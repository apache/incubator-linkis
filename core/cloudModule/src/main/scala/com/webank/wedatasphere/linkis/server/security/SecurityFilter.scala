/*
 * Copyright 2019 WeBank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.wedatasphere.linkis.server.security

import java.text.DateFormat
import java.util.{Date, Locale}

import com.webank.wedatasphere.linkis.common.conf.{CommonVars, Configuration}
import com.webank.wedatasphere.linkis.common.utils.{Logging, RSAUtils, Utils}
import com.webank.wedatasphere.linkis.server.conf.ServerConfiguration
import com.webank.wedatasphere.linkis.server.exception.{IllegalUserTicketException, LoginExpireException, NonLoginException}
import com.webank.wedatasphere.linkis.server.security.SSOUtils.sslEnable
import com.webank.wedatasphere.linkis.server.{Message, _}
import javax.servlet._
import javax.servlet.http.{Cookie, HttpServletRequest, HttpServletResponse}
import org.apache.commons.lang.StringUtils

/**
  * Created by enjoyyin on 2018/1/9.
  */
class SecurityFilter extends Filter with Logging {
  private val refererValidate = ServerConfiguration.BDP_SERVER_SECURITY_REFERER_VALIDATE.getValue
  private val localAddress = ServerConfiguration.BDP_SERVER_ADDRESS.getValue
  protected val testUser = ServerConfiguration.BDP_TEST_USER.getValue
  private val PASS_AUTH_REQUEST_URI = CommonVars("wds.linkis.conf.url.pass.auth",
    ServerConfiguration.BDP_SERVER_RESTFUL_URI.getValue + "/oneservice/execute/").getValue.split(",")

  override def init(filterConfig: FilterConfig): Unit = {}

  private def filterResponse(message: Message)(implicit response: HttpServletResponse): Unit = {
    response.setHeader("Content-Type", "application/json;charset=UTF-8")
    response.setStatus(Message.messageToHttpStatus(message))
    response.getOutputStream.print(message)
    response.getOutputStream.flush()
  }

  def doFilter(request: HttpServletRequest)(implicit response: HttpServletResponse): Boolean = {
    addAccessHeaders(response)
    if(refererValidate) {
      //Security certification support, referer limited(安全认证支持，referer限定)
      val referer = request.getHeader("Referer")
      if(StringUtils.isNotEmpty(referer) && !referer.trim.contains(localAddress)) {
        filterResponse(validateFailed("不允许的跨站请求！"))
        return false
      }
      //Security certification support, solving verb tampering(安全认证支持，解决动词篡改)
      request.getMethod.toUpperCase match {
        case "GET" | "POST" | "PUT" | "DELETE" | "HEAD" | "TRACE" | "CONNECT" | "OPTIONS" =>
        case _ =>
          filterResponse(validateFailed("Do not use HTTP verbs to tamper with!(不可使用HTTP动词篡改！)"))
          return false
      }
    }

    val isPassAuthRequest = PASS_AUTH_REQUEST_URI.exists(request.getRequestURI.startsWith)
    if(request.getRequestURI == ServerConfiguration.BDP_SERVER_SECURITY_SSL_URI.getValue) {
      val message = Message.ok("Get success!(获取成功！)").data("enable", SSOUtils.sslEnable)
      if(SSOUtils.sslEnable) message.data("publicKey", RSAUtils.getDefaultPublicKey())
      filterResponse(message)
      false
    } else if(request.getRequestURI == ServerConfiguration.BDP_SERVER_RESTFUL_LOGIN_URI.getValue) {
      true
    } else if(isPassAuthRequest) {
      logger.info("No login needed for uri: " + request.getRequestURI)
      true
    } else {
      val userName = Utils.tryCatch(SecurityFilter.getLoginUser(request)){
        case n: NonLoginException =>
          if(Configuration.IS_TEST_MODE.getValue) None else {
            filterResponse(Message.noLogin(n.getMessage) << request.getRequestURI)
            return false
          }
        case t: Throwable =>
          SecurityFilter.warn("", t)
          throw t
      }
      if(userName.isDefined) {
        true
      } else if(Configuration.IS_TEST_MODE.getValue) {
        SecurityFilter.info("test mode! login for uri: " + request.getRequestURI)
        SecurityFilter.setLoginUser(response, testUser)
        true
      } else {
        filterResponse(Message.noLogin("You are not logged in, please login first!(您尚未登录，请先登录!)") << request.getRequestURI)
        false
      }
    }
  }

  override def doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain): Unit = {
    val request = servletRequest.asInstanceOf[HttpServletRequest]
    implicit val response = servletResponse.asInstanceOf[HttpServletResponse]
    if(doFilter(request)) filterChain.doFilter(servletRequest, servletResponse)
    if(SecurityFilter.isRequestIgnoreTimeout(request)) SecurityFilter.removeIgnoreTimeoutSignal(response)
  }

  protected def addAccessHeaders(response: HttpServletResponse) {
    response.setHeader("Access-Control-Allow-Origin", "*")
    response.setHeader("Access-Control-Allow-Credentials", "true")
    response.setHeader("Access-Control-Allow-Headers", "authorization,Content-Type")
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, HEAD, DELETE")
    val fullDateFormatEN = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale("EN", "en"))
    response.setHeader("Date", fullDateFormatEN.format(new Date))
  }

  override def destroy(): Unit = {}
}

object SecurityFilter extends Logging {
  private[linkis] val OTHER_SYSTEM_IGNORE_UM_USER = "dataworkcloud_rpc_user"
  private[linkis] val ALLOW_ACCESS_WITHOUT_TIMEOUT = "dataworkcloud_inner_request"
  def getLoginUserThrowsExceptionWhenTimeout(req: HttpServletRequest): Option[String] = Option(req.getCookies).flatMap(cs => SSOUtils.getLoginUser(cs))
    .orElse(SSOUtils.getLoginUserIgnoreTimeout(key => Option(req.getHeader(key))).filter(_ == OTHER_SYSTEM_IGNORE_UM_USER))
  def getLoginUser(req: HttpServletRequest): Option[String] = Utils.tryCatch(getLoginUserThrowsExceptionWhenTimeout(req)) {
    case _: LoginExpireException =>
      SSOUtils.getLoginUserIgnoreTimeout(key => Option(req.getCookies).flatMap(_.find(_.getName == key).map(_.getValue))).filter(user => user != OTHER_SYSTEM_IGNORE_UM_USER &&
        isRequestIgnoreTimeout(req))
    case t => throw t
  }
  def isRequestIgnoreTimeout(req: HttpServletRequest): Boolean = Option(req.getCookies).exists(_.exists(c => c.getName == ALLOW_ACCESS_WITHOUT_TIMEOUT && c.getValue == "true"))
  def addIgnoreTimeoutSignal(response: HttpServletResponse): Unit = response.addCookie(ignoreTimeoutSignal())
  def ignoreTimeoutSignal(): Cookie = {
    val cookie = new Cookie(ALLOW_ACCESS_WITHOUT_TIMEOUT, "true")
    cookie.setMaxAge(-1)
    cookie.setPath("/")
    if(sslEnable) cookie.setSecure(true)
    cookie
  }
  def removeIgnoreTimeoutSignal(response: HttpServletResponse): Unit = {
    val cookie = new Cookie(ALLOW_ACCESS_WITHOUT_TIMEOUT, "false")
    cookie.setMaxAge(0)
    cookie.setPath("/")
    if(sslEnable) cookie.setSecure(true)
    response.addCookie(cookie)
  }
  def getLoginUsername(req: HttpServletRequest): String = getLoginUser(req).getOrElse(throw new IllegalUserTicketException( s"Illegal user token information(非法的用户token信息)."))
  def setLoginUser(resp: HttpServletResponse, username: String): Unit = SSOUtils.setLoginUser(c => resp.addCookie(c), username)
  def removeLoginUser(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    SSOUtils.removeLoginUser(req.getCookies)
    SSOUtils.removeLoginUserByAddCookie(s => resp.addCookie(s))
  }
}