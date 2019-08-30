package com.aristo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.nytimes.testng.Logger;

public class TestAccountsProvider {

	private static Connection conn = null;
	private static String configFile = "AutoTestDataConfig";
	public static ArrayList<String> activeHDCache = null;
	public static boolean flag = false;
	static Session session;

	public TestAccountsProvider() throws Exception {
		System.out.println("TestDataProvider Started...");
		if (flag == false) {
		prefetchCache();
		closeConnection();

		flag = true;
		}
	}

	static Map<String, String> properties = YamlUtil.getTestData(configFile, "SSH_STG_Properties");

	private static void doSshTunnel(String strSshUser, String prvkey, String strSshHost, int nSshPort,
			String strRemoteHost, int nLocalPort, int nRemotePort) throws JSchException {
		final JSch jsch = new JSch();

		jsch.addIdentity(prvkey);
		session = jsch.getSession(strSshUser, strSshHost, 22);

		final Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);

		session.connect();
		session.setPortForwardingL(nLocalPort, strRemoteHost, nRemotePort);
	}

	public static void connectSSH() throws Exception {
		Object result = new Object();
		try {
			String strSshUser = properties.get("qa_strSshUser");
			
			String prvkey = System.getProperty("user.dir")+"//privatekey//"+"//PrivateKeyPrakhya.ppk";
			String strSshHost = properties.get("qa_strSshHost");
			int nSshPort = Integer.parseInt(properties.get("qa_nSshPort"));
			String strRemoteHost = properties.get("qa_strRemoteHost");
			int nLocalPort = Integer.parseInt(properties.get("qa_nLocalPort"));
			int nRemotePort = Integer.parseInt(properties.get("qa_nRemotePort"));

			doSshTunnel(strSshUser, prvkey, strSshHost, nSshPort, strRemoteHost, nLocalPort, nRemotePort);
		} catch (Exception e) {
		//	e.printStackTrace();
		} finally {
		}
	}

	private static void CloseSSHConnection() {
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

	public static boolean openConnection() throws Exception {
		boolean flag = false;
		connectSSH();
		String strDbUser = properties.get("qa_strDbUser");
		String strDbPassword = properties.get("qa_strDbPassword");
		String connection_url = properties.get("connection_url");
		conn = DriverManager.getConnection(connection_url, strDbUser, strDbPassword);
		if (!conn.isClosed()) {
			System.out.println("DB Connection Opened...");
			flag = true;
		}
		return flag;
	}

	public static boolean closeConnection() throws Exception {
		boolean flag = false;
		if (!conn.isClosed()) {
			conn.close();
			flag = true;
			System.out.println("DB Connection Closed...");
			CloseSSHConnection();
		}
		return flag;
	}

		/*private String buildQuery(String query, Map<String, String> criteria) {
		Map<String, String> keysToColumnsMap = YamlUtil.getTestData(configFile, "KeyColumnMappings");
		String queryHead = YamlUtil.getTestData(configFile, query).get("genericHead");
		String queryTail = YamlUtil.getTestData(configFile, query).get("genericTail");
		// Random rd = new Random();
		// int[] acctStart = {40,80};
		// int choice = acctStart[rd.nextInt(2)]+rd.nextInt(10);
		// queryTail += " and SUB_ACCT_NUM like '"+choice+"%'";
		StringBuilder queryConditions = new StringBuilder("");
		
		 * Map<String, String> keyColumnMapping = new HashMap<String, String>();
		 * keyColumnMapping.put("status", "p.PROD_STATUS_CD_P");
		 * keyColumnMapping.put("deliveryType", "DELIVERY_TYPE_P");
		 * keyColumnMapping.put("promo", "PROM_RATE_CD_P");
		 
		for (String key : criteria.keySet()) {
			if (keysToColumnsMap.containsKey(key)) {
				queryConditions.append(" and " + keysToColumnsMap.get(key) + "=" + "'" + criteria.get(key) + "'");
			}
		}
		queryConditions.append(queryTail);
		String finalQuery = queryHead + queryConditions;
		System.out.println("BUILD_QUERY: " + finalQuery);
		return finalQuery;
	}*/

	public void prefetchCache() throws Exception {
		openConnection();
		Map<String, String> queries = YamlUtil.getTestData(configFile, "CacheAccountsEligibleForVr");
		int cacheSize = Integer.parseInt(queries.get("cacheSize"));
		System.out.println("Building cache for Active HD accounts eligible for VR");
		activeHDCache = getAccountsData(queries.get("HD"), cacheSize);
		System.out.println("Caching successful...");
		closeConnection();
	}

	/* This method is used to build cache */
	public ArrayList<String> getAccountsData(String query, int size) throws Exception {
		ArrayList<String> temp = new ArrayList<String>();
		Statement stmt = conn.createStatement();
		stmt.setMaxRows(size);
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			temp.add(rs.getString(1));
		}
		stmt.close();
		return temp;
	}

	/*
	 * To method is used to get active account number based on delivery types
	 * from cache
	 */
	public String getAccount(String deliveryType) {
		Logger.info("Test get");
		String account = new String();
		if (activeHDCache.size() > 0) {
			account = activeHDCache.get(0);
			activeHDCache.remove(account);		
		} else {
			System.out.println("No Active account cache available for " + deliveryType);
		}
		return account;
	}

	/*
	 * To get active account number for HD, SM and DD deliveryTypes using search
	 * criteria
	 */
	/*public String getAccount(String query, Map<String, String> criteria) throws Exception {
		Random randomNo = new Random();
		ArrayList<String> accounts = new ArrayList<String>();
		openConnection();
		String accountNo = "";
		Statement stmt = conn.createStatement();
		stmt.setMaxRows(6);
		ResultSet rs = stmt.executeQuery(buildQuery(query, criteria));
		while (rs.next()) {
			accounts.add(rs.getString(1));
			System.out.println(accounts);
		}
		System.out.println("Length Temp accounts:" + accounts.size());

		accountNo = accounts.get(randomNo.nextInt(5));
		System.out.println("Account picked is" + accountNo);
		accounts.clear();
		closeConnection();
		return accountNo;
	}*/
	
	// Alternate method to connect to db without string builder
	/*public String getDetailsFromAccount(String query) throws Exception {
		String details = null;
		//openConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
		details = rs.getString(1);
		}
		return details;
		}
	
	public ResultSet getRTEDetails(String query ) throws Exception {
	 //  	openConnection();
	   	Statement stmt = conn.createStatement();
	   	stmt.setMaxRows(1);
	   	ResultSet rs = stmt.executeQuery(query);
	   	return rs;
	   }*/
}