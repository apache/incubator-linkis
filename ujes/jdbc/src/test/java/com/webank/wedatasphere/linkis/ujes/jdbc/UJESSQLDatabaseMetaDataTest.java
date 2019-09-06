package com.webank.wedatasphere.linkis.ujes.jdbc;
/**
 * Created by owenxu on 2019/8/23.
 */

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;


public class UJESSQLDatabaseMetaDataTest {
    private static UJESSQLConnection conn;
    private static UJESSQLDatabaseMetaData dbmd;
    
    @BeforeClass
    public static void preWork(){
        try {
            conn = CreateConnection.getConnection();
            dbmd = (UJESSQLDatabaseMetaData) conn.getMetaData();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = UJESSQLException.class)
    public void supportsMinimumSQLGrammar() {
        dbmd.supportsMinimumSQLGrammar();
    }

    @Test(expected = UJESSQLException.class)
    public void getResultSetHoldability() {
        dbmd.getResultSetHoldability();
    }

    @Test(expected = UJESSQLException.class)
    public void getMaxColumnsInGroupBy() {
        dbmd.getMaxColumnsInGroupBy();
    }

    @Test(expected = UJESSQLException.class)
    public void supportsSubqueriesInComparisons() {
        dbmd.supportsSubqueriesInComparisons();
    }

    @Test(expected = UJESSQLException.class)
    public void getMaxColumnsInSelect() {
        dbmd.getMaxColumnsInSelect();
    }

    @Test(expected = UJESSQLException.class)
    public void nullPlusNonNullIsNull() {
        dbmd.nullPlusNonNullIsNull();
    }

    @Test
    public void supportsCatalogsInDataManipulation() {
        assertFalse(dbmd.supportsCatalogsInDataManipulation());
    }

    @Test(expected = UJESSQLException.class)
    public void supportsDataDefinitionAndDataManipulationTransactions() {
        dbmd.supportsDataDefinitionAndDataManipulationTransactions();
    }

    @Test(expected = UJESSQLException.class)
    public void supportsTableCorrelationNames() {
        dbmd.supportsTableCorrelationNames();
    }

    @Test
    public void getDefaultTransactionIsolation() {
        assertEquals(dbmd.getDefaultTransactionIsolation(),0);
    }

    @Test
    public void supportsFullOuterJoins() {
        assertTrue(dbmd.supportsFullOuterJoins());
    }

    @Test(expected = UJESSQLException.class)
    public void supportsExpressionsInOrderBy() {
        dbmd.supportsExpressionsInOrderBy();
    }

    @Test
    public void allProceduresAreCallable() {
        assertFalse(dbmd.allProceduresAreCallable());
    }

    @Test(expected = UJESSQLException.class)
    public void getMaxTablesInSelect() {
        dbmd.getMaxTablesInSelect();
    }

    @Test(expected = UJESSQLException.class)
    public void nullsAreSortedAtStart() {
        dbmd.nullsAreSortedAtStart();
    }

    @Test
    public void supportsPositionedUpdate() {
        assertFalse(dbmd.supportsPositionedUpdate());
    }

    @Test(expected = UJESSQLException.class)
    public void ownDeletesAreVisible() {
        dbmd.ownDeletesAreVisible(0);
    }

    @Test
    public void supportsResultSetHoldability() {
        assertFalse(dbmd.supportsResultSetHoldability(0));
    }

    @Test(expected = UJESSQLException.class)
    public void getMaxStatements() {
        dbmd.getMaxStatements();
    }

    @Test(expected = UJESSQLException.class)
    public void getRowIdLifetime() {
        dbmd.getRowIdLifetime();
    }

    @Test
    public void getDriverVersion() {
        assertEquals(dbmd.getDriverVersion(), String.valueOf(UJESSQLDriverMain.DEFAULT_VERSION()));
    }

    @AfterClass
    public static void closeStateAndConn(){
        conn.close();
        dbmd = null;
    }

}