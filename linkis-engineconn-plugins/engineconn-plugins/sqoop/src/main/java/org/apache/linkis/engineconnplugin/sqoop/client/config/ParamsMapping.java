package org.apache.linkis.engineconnplugin.sqoop.client.config;

import java.util.HashMap;
import java.util.Map;

public final class ParamsMapping {
    public static Map<String,String> mapping;
    static {
        mapping = new HashMap<>();
        mapping.put("sqoop.args.connect","--connect");
        mapping.put("sqoop.args.connection.manager","--connection-manager");
        mapping.put("sqoop.args.connection.param.file","--connection-param-file");
        mapping.put("sqoop.args.driver","--driver");
        mapping.put("sqoop.args.hadoop.home","--hadoop-home");
        mapping.put("sqoop.args.hadoop.mapred.home","--hadoop-mapred-home");
        mapping.put("sqoop.args.help","--help");
        mapping.put("sqoop.args.password","--password");
        mapping.put("sqoop.args.password.alias","--password-alias");
        mapping.put("sqoop.args.password.file","--password-file");
        mapping.put("sqoop.args.relaxed.isolation","--relaxed-isolation");
        mapping.put("sqoop.args.skip.dist.cache","--skip-dist-cache");
        mapping.put("sqoop.args.username","--username");
        mapping.put("sqoop.args.verbose","--verbose");
        mapping.put("sqoop.args.append","--append");
        mapping.put("sqoop.args.as.avrodatafile","--as-avrodatafile");
        mapping.put("sqoop.args.as.parquetfile","--as-parquetfile");
        mapping.put("sqoop.args.as.sequencefile","--as-sequencefile");
        mapping.put("sqoop.args.as.textfile","--as-textfile");
        mapping.put("sqoop.args.autoreset.to.one.mapper","--autoreset-to-one-mapper");
        mapping.put("sqoop.args.boundary.query","--boundary-query");
        mapping.put("sqoop.args.case.insensitive","--case-insensitive");
        mapping.put("sqoop.args.columns","--columns");
        mapping.put("sqoop.args.compression.codec","--compression-codec");
        mapping.put("sqoop.args.delete.target.dir","--delete-target-dir");
        mapping.put("sqoop.args.direct","--direct");
        mapping.put("sqoop.args.direct.split.size","--direct-split-size");
        mapping.put("sqoop.args.query","--query");
        mapping.put("sqoop.args.fetch.size","--fetch-size");
        mapping.put("sqoop.args.inline.lob.limit","--inline-lob-limit");
        mapping.put("sqoop.args.num.mappers","--num-mappers");
        mapping.put("sqoop.args.mapreduce.job.name","--mapreduce-job-name");
        mapping.put("sqoop.args.merge.key","--merge-key");
        mapping.put("sqoop.args.split.by","--split-by");
        mapping.put("sqoop.args.table","--table");
        mapping.put("sqoop.args.target.dir","--target-dir");
        mapping.put("sqoop.args.validate","--validate");
        mapping.put("sqoop.args.validation.failurehandler","--validation-failurehandler");
        mapping.put("sqoop.args.validation.threshold"," --validation-threshold");
        mapping.put("sqoop.args.validator","--validator");
        mapping.put("sqoop.args.warehouse.dir","--warehouse-dir");
        mapping.put("sqoop.args.where","--where");
        mapping.put("sqoop.args.compress","--compress");
        mapping.put("sqoop.args.check.column","--check-column");
        mapping.put("sqoop.args.incremental","--incremental");
        mapping.put("sqoop.args.last.value","--last-value");
        mapping.put("sqoop.args.enclosed.by","--enclosed-by");
        mapping.put("sqoop.args.escaped.by","--escaped-by");
        mapping.put("sqoop.args.fields.terminated.by","--fields-terminated-by");
        mapping.put("sqoop.args.lines.terminated.by","--lines-terminated-by");
        mapping.put("sqoop.args.mysql.delimiters","--mysql-delimiters");
        mapping.put("sqoop.args.optionally.enclosed.by","--optionally-enclosed-by");
        mapping.put("sqoop.args.input.enclosed.by","--input-enclosed-by");
        mapping.put("sqoop.args.input.escaped.by","--input-escaped-by");
        mapping.put("sqoop.args.input.fields.terminated.by","--input-fields-terminated-by");
        mapping.put("sqoop.args.input.lines.terminated.by","--input-lines-terminated-by");
        mapping.put("sqoop.args.input.optionally.enclosed.by","--input-optionally-enclosed-by");
        mapping.put("sqoop.args.create.hive.table","---create-hive-table");
        mapping.put("sqoop.args.hive.delims.replacement","--hive-delims-replacement");
        mapping.put("sqoop.args.hive.database","--hive-database");
        mapping.put("sqoop.args.hive.drop.import.delims","--hive-drop-import-delims");
        mapping.put("sqoop.args.hive.home","--hive-home");
        mapping.put("sqoop.args.hive.import","--hive-import");
        mapping.put("sqoop.args.hive.overwrite","--hive-overwrite");
        mapping.put("sqoop.args.hive.partition.value","--hive-partition-value");
        mapping.put("sqoop.args.hive.table","--hive-table");
        mapping.put("sqoop.args.column.family","--column-family");
        mapping.put("sqoop.args.hbase.bulkload","--hbase-bulkload");
        mapping.put("sqoop.args.hbase.create.table","--hbase-create-table");
        mapping.put("sqoop.args.hbase.row.key","--hbase-row-key");
        mapping.put("sqoop.args.hbase.table","--hbase-table");
        mapping.put("sqoop.args.hcatalog.database","--hcatalog-database");
        mapping.put("sqoop.args.hcatalog.home","--hcatalog-home");
        mapping.put("sqoop.args.hcatalog.partition.keys","--hcatalog-partition-keys");
        mapping.put("sqoop.args.hcatalog.partition.values","--hcatalog-partition-values");
        mapping.put("sqoop.args.hcatalog.table","--hcatalog-table");
        mapping.put("sqoop.args.hive.partition.key","--hive-partition-key");
        mapping.put("sqoop.args.map.column.hive","--map-column-hive");
        mapping.put("sqoop.args.create.hcatalog.table","--create-hcatalog-table");
        mapping.put("sqoop.args.hcatalog.storage.stanza","--hcatalog-storage-stanza");
        mapping.put("sqoop.args.accumulo.batch.size","--accumulo-batch-size");
        mapping.put("sqoop.args.accumulo.column.family","--accumulo-column-family");
        mapping.put("sqoop.args.accumulo.create.table","--accumulo-create-table");
        mapping.put("sqoop.args.accumulo.instance","--accumulo-instance");
        mapping.put("sqoop.args.accumulo.max.latency","--accumulo-max-latency");
        mapping.put("sqoop.args.accumulo.password","--accumulo-password");
        mapping.put("sqoop.args.accumulo.row.key","--accumulo-row-key");
        mapping.put("sqoop.args.accumulo.table","--accumulo-table");
        mapping.put("sqoop.args.accumulo.user","--accumulo-user");
        mapping.put("sqoop.args.accumulo.visibility","--accumulo-visibility");
        mapping.put("sqoop.args.accumulo.zookeepers","--accumulo-zookeepers");
        mapping.put("sqoop.args.bindir","--bindir");
        mapping.put("sqoop.args.class.name","--class-name");
        mapping.put("sqoop.args.input.null.non.string","--input-null-non-string");
        mapping.put("sqoop.args.input.null.string","--input-null-string");
        mapping.put("sqoop.args.jar.file","--jar-file");
        mapping.put("sqoop.args.map.column.java","--map-column-java");
        mapping.put("sqoop.args.null.non.string","--null-non-string");
        mapping.put("sqoop.args.null.string","--null-string");
        mapping.put("sqoop.args.outdir","--outdir");
        mapping.put("sqoop.args.package.name","--package-name");
        mapping.put("sqoop.args.conf","-conf");
        mapping.put("sqoop.args.D","-D");
        mapping.put("sqoop.args.fs","-fs");
        mapping.put("sqoop.args.jt","-jt");
        mapping.put("sqoop.args.files","-files");
        mapping.put("sqoop.args.libjars","-libjars");
        mapping.put("sqoop.args.archives","-archives");
    }
}
