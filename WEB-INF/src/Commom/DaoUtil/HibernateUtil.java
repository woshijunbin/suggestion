package Commom.DaoUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import Commom.Util.ReflectionUtil;
import Commom.Util.ShowPage;
public final class HibernateUtil {
	private HibernateUtil(){}
	private static final SessionFactory SESSIONFACTORY ;
	static{
		final Configuration cfg=new Configuration();
		cfg.configure("configure/hibernate.cfg.xml");
		final ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		SESSIONFACTORY=cfg.buildSessionFactory(serviceRegistry);
	}
	public static Session getSession() {
		return SESSIONFACTORY.openSession();
	}
	public static void closeSession(Session session) {
		if (session.isOpen()) {
			session.close();
		}
	}
	/**
	 * 添加数据
	 * @param o 实体对象	(需与数据库完全匹配)
	 * @return true | false
	 */
	public static boolean insertData(Object o){
		Map<String, Object> map = new ReflectionUtil().getFieldList(o);
		if (map.size() == 0) {
			return false;
		}
		String sql = "INSERT INTO " + o.getClass().getSimpleName().toUpperCase() + "(";
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		Iterator<Entry<String, Object>> entryKeyIterator = map.entrySet().iterator();
        while (entryKeyIterator.hasNext()) {
            Entry<String, Object> e = entryKeyIterator.next();
            String key = e.getKey();
			sb1.append("," + key);
			sb2.append(",?");
			list.add(e.getValue());
        }
		sql += (sb1.length() > 1 ? sb1.substring(1) : sb1) 
			+ ")VALUES(" + (sb2.length() > 1 ? sb2.substring(1) : sb2)
			+ ")";
		Session session = getSession();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			int index = 0;
			for (Object val : list) {
				query.setParameter(index++, val);
			}
			return query.executeUpdate() > 0;
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			closeSession(session);
		}
		return false;
	}
	/**
	 * 添加数据
	 * @param session
	 * @param o 实体对象	(需与数据库完全匹配)
	 * @return true | false
	 */
	public static boolean insertData(Session session, Object o){
		Map<String, Object> map = new ReflectionUtil().getFieldList(o);
		if (map.size() == 0) {
			return false;
		}
		String sql = "INSERT INTO " + o.getClass().getSimpleName().toUpperCase() + "(";
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		Iterator<Entry<String, Object>> entryKeyIterator = map.entrySet().iterator();
        while (entryKeyIterator.hasNext()) {
            Entry<String, Object> e = entryKeyIterator.next();
            String key = e.getKey();
			sb1.append("," + key);
			sb2.append(",?");
			list.add(e.getValue());
        }
		sql += (sb1.length() > 1 ? sb1.substring(1) : sb1) 
			+ ")VALUES(" + (sb2.length() > 1 ? sb2.substring(1) : sb2)
			+ ")";
		SQLQuery query = session.createSQLQuery(sql);
		int index = 0;
		for (Object val : list) {
			query.setParameter(index++, val);
		}
		return query.executeUpdate() > 0;
	}
	/**
	 * 添加数据(临时表插入正式表)
	 * @param session
	 * @param o 实体对象	(需与数据库完全匹配)
	 * @param where		不对应的字段名(where条件，如"字段名,字段名"，多个用逗号隔开)
	 * @return true | false
	 */
	public static boolean insertData(Session session, Object o, String noWhere){
		Map<String, Object> map = new ReflectionUtil().getFieldList(o);
		if (map.size() == 0) {
			return false;
		}
		String sql = "INSERT INTO " + o.getClass().getSimpleName().toUpperCase().replace("LSB", "") + "(";
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		Iterator<Entry<String, Object>> entryKeyIterator = map.entrySet().iterator();
        while (entryKeyIterator.hasNext()) {
            Entry<String, Object> e = entryKeyIterator.next();
            String key = e.getKey();
            if (noWhere.indexOf(key) == -1) {
				sb1.append("," + key);
				sb2.append(",?");
				list.add(e.getValue());
            }
        }
		sql += (sb1.length() > 1 ? sb1.substring(1) : sb1) 
			+ ")VALUES(" + (sb2.length() > 1 ? sb2.substring(1) : sb2)
			+ ")";
		SQLQuery query = session.createSQLQuery(sql);
		int index = 0;
		for (Object val : list) {
			query.setParameter(index++, val);
		}
		return query.executeUpdate() > 0;
	}
	/**
	 * 添加记录表数据
	 * @param session
	 * @param o 实体对象	(需与数据库完全匹配)
	 * @param where		不对应的字段名(where条件，如"字段名,字段名"，多个用逗号隔开)
	 * @return true | false
	 */
	public static boolean insertData2(Session session, Object o, String noWhere){
		Map<String, Object> map = new ReflectionUtil().getFieldList(o);
		if (map.size() == 0) {
			return false;
		}
		String sql = "INSERT INTO " + o.getClass().getSimpleName().toUpperCase() + "_JLB (";
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		Iterator<Entry<String, Object>> entryKeyIterator = map.entrySet().iterator();
        while (entryKeyIterator.hasNext()) {
            Entry<String, Object> e = entryKeyIterator.next();
            String key = e.getKey();
            if (noWhere.indexOf(key) == -1) {
				sb1.append("," + key);
				sb2.append(",?");
				list.add(e.getValue());
            }
        }
		sql += (sb1.length() > 1 ? sb1.substring(1) : sb1) 
			+ ")VALUES(" + (sb2.length() > 1 ? sb2.substring(1) : sb2)
			+ ")";
		SQLQuery query = session.createSQLQuery(sql);
		int index = 0;
		for (Object val : list) {
			query.setParameter(index++, val);
		}
		return query.executeUpdate() > 0;
	}
	/**
	 * 修改数据
	 * @param session
	 * @param o 实体对象	(需与数据库完全匹配)
	 * @param where		(where条件，如"字段名,字段名"，多个用逗号隔开)
	 * @return true | false
	 */
	public static boolean updateData(Session session, Object o, String where){
		Map<String, Object> map = new ReflectionUtil().getFieldList(o);
		if (map.size() == 0 && where.length() > 0) {
			return false;
		}
		String sql = "UPDATE " + o.getClass().getSimpleName().toUpperCase() + " SET ";
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		List<Object> list1 = new ArrayList<Object>();
		List<Object> list2 = new ArrayList<Object>();
		where = where.toUpperCase();
		Iterator<Entry<String, Object>> entryKeyIterator = map.entrySet().iterator();
        while (entryKeyIterator.hasNext()) {
            Entry<String, Object> e = entryKeyIterator.next();
            String key = e.getKey();
            if (where.indexOf(key) > -1) {
				sb2.append(" AND " + key + " LIKE ?");
				list2.add(e.getValue());
			} else {
				sb1.append("," + key + " = ?");
				list1.add(e.getValue());
			}
        }
		sql += (sb1.length() > 1 ? sb1.substring(1) : sb1) 
			+ " WHERE 1=1 " + sb2;
		SQLQuery query = session.createSQLQuery(sql);
		int index = 0;
		for (Object val : list1) {
			query.setParameter(index++, val);
		}
		for (Object val : list2) {
			query.setParameter(index++, val);
		}
		return query.executeUpdate() > 0;
	}
	/**
	 * 修改数据
	 * @param o 实体对象	(需与数据库完全匹配)
	 * @param where		(where条件，如"字段名,字段名"，多个用逗号隔开)
	 * @return true | false
	 */
	public static boolean updateData(Object o, String where){
		Map<String, Object> map = new ReflectionUtil().getFieldList(o);
		if (map.size() == 0 && where.length() > 0) {
			return false;
		}
		String sql = "UPDATE " + o.getClass().getSimpleName().toUpperCase() + " SET ";
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		List<Object> list1 = new ArrayList<Object>();
		List<Object> list2 = new ArrayList<Object>();
		where = where.toUpperCase();
		Iterator<Entry<String, Object>> entryKeyIterator = map.entrySet().iterator();
        while (entryKeyIterator.hasNext()) {
            Entry<String, Object> e = entryKeyIterator.next();
            String key = e.getKey();
            if (where.indexOf(key) > -1) {
				sb2.append(" AND " + key + " LIKE ?");
				list2.add(e.getValue());
			} else {
				sb1.append("," + key + " = ?");
				list1.add(e.getValue());
			}
        }
		sql += (sb1.length() > 1 ? sb1.substring(1) : sb1) + " WHERE 1=1 " + sb2;
		Session session = getSession();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			int index = 0;
			for (Object val : list1) {
				query.setParameter(index++, val);
			}
			for (Object val : list2) {
				query.setParameter(index++, val);
			}
			return query.executeUpdate() > 0;
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			closeSession(session);
		}
		return false;
	}
	/**
	 * 修改数据
	 * @param sql		SQL语句
	 * @param where		(参数数组)
	 * @return			true | false
	 */
	public static boolean updateData(String sql, Object[] where){
		Session session = getSession();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			if (where != null) {
				int index = 0;
				for (Object o : where) {
					query.setParameter(index++, o);
				}
			}
			return query.executeUpdate() > 0;
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			closeSession(session);
		}
		return false;
	}
	/**
	 * 修改数据
	 * @param session
	 * @param sql		(SQL语句)
	 * @param where		(参数数组)
	 * @return			true | false
	 */
	public static boolean updateData(Session session, String sql, Object[] where){
		SQLQuery query = session.createSQLQuery(sql);
		if (where != null) {
			int index = 0;
			for (Object o : where) {
				query.setParameter(index++, o);
			}
		}
		return query.executeUpdate() > 0;
	}
	/**
	 * 删除数据
	 * @param clazz
	 * @param where	(where[0]为对应的字段，where[1]为参数值)
	 * @return true | false
	 */
	@SuppressWarnings("rawtypes")
	public static boolean deleteData(Class clazz, Object[][] where){
		if (where != null && where[0].length > 0) {
			StringBuffer sql = new StringBuffer("DELETE FROM " + clazz.getSimpleName().toUpperCase() + " WHERE 1=1 ");
			for (int i = 0; i < where[0].length; i++) {
				if (where[0][i].toString().indexOf("?") > -1) {
					sql.append(" AND " + where[0][i]);
				} else {
					sql.append(" AND " + where[0][i] + " LIKE ?");
				}
			}
			Session session = getSession();
			try {
				SQLQuery query = session.createSQLQuery(sql.toString());
				int index = 0;
				for (int i = 0; i < where[1].length; i++) {
					if (where[1][i].getClass().isArray()) {
						for (Object o : (Object[])where[1][i]) {
							query.setParameter(index++, o);
						}
					} else {
						query.setParameter(index++, where[1][i]);
					}
				}
				return query.executeUpdate() > 0;
			} catch (HibernateException e) {
				e.printStackTrace();
			} finally {
				closeSession(session);
			}
		}
		return false;
	}
	/**
	 * 删除数据
	 * @param session
	 * @param clazz
	 * @param where	(where[0]为对应的字段，where[1]为参数值)
	 * @return true | false
	 */
	@SuppressWarnings("rawtypes")
	public static boolean deleteData(Session session, Class clazz, Object[][] where){
		if (where != null && where[0].length > 0) {
			StringBuffer sql = new StringBuffer("DELETE FROM " + clazz.getSimpleName().toUpperCase() + " WHERE 1=1 ");
			for (int i = 0; i < where[0].length; i++) {
				if (where[0][i].toString().indexOf("?") > -1) {
					sql.append(" AND " + where[0][i]);
				} else {
					sql.append(" AND " + where[0][i] + " LIKE ?");
				}
			}
			SQLQuery query = session.createSQLQuery(sql.toString());
			int index = 0;
			for (int i = 0; i < where[1].length; i++) {
				if (where[1][i].getClass().isArray()) {
					for (Object o : (Object[])where[1][i]) {
						query.setParameter(index++, o);
					}
				} else {
					query.setParameter(index++, where[1][i]);
				}
			}
			return query.executeUpdate() > 0;
		}
		return false;
	}
	/**
	 * 查询数据
	 * @param session
	 * @param clazz
	 * @return	对象集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List listData(Class clazz){
		String sql = "SELECT * FROM " + clazz.getSimpleName().toUpperCase();
		Session session = HibernateUtil.getSession();
		List<Object> lis = new ArrayList<Object>();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			lis = query.addEntity(clazz).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return lis;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param where		搜索条件
	 * @param orderStr	搜索条件
	 * @return	对象集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List listData(Class clazz, Object[][] where, String... orderStr){
		StringBuffer sql = new StringBuffer("SELECT ? FROM " + clazz.getSimpleName().toUpperCase() + " WHERE 1=1 ");
		if (where != null) {
			for (int i = 0; i < where[0].length; i++) {
				if (where[0][i].toString().indexOf("?") > -1) {
					sql.append(" AND " + where[0][i]);
				} else {
					sql.append(" AND " + where[0][i] + " LIKE ?");
				}
			}
		}
		for (String s : orderStr) {
			sql.append(" " + s + " ");
		}
		Session session = HibernateUtil.getSession();
		List<Object> lis = new ArrayList<Object>();
		try {
			SQLQuery query = session.createSQLQuery(sql.toString().replaceFirst("[?]", "*"));
			if (where != null) {
				int index = 0;
				for (int i = 0; i < where[1].length; i++) {
					if (where[1][i].getClass().isArray()) {
						for (Object o : (Object[])where[1][i]) {
							query.setParameter(index++, o);
						}
					} else {
						query.setParameter(index++, where[1][i]);
					}
				}
			}
			lis = query.addEntity(clazz).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return lis;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param joinStr	表关联语句
	 * @param where		搜索条件
	 * @return	对象集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List listData(Class clazz, String joinStr, Object[][] where){
		StringBuffer sql = new StringBuffer("SELECT * FROM " + clazz.getSimpleName().toUpperCase() + " " + joinStr +" WHERE 1=1 ");
		if (where != null) {
			for (int i = 0; i < where[0].length; i++) {
				if (where[0][i].toString().indexOf("?") > -1) {
					sql.append(" AND " + where[0][i]);
				} else {
					sql.append(" AND " + where[0][i] + " LIKE ?");
				}
			}
		}
		Session session = HibernateUtil.getSession();
		List<Object> lis = new ArrayList<Object>();
		try {
			SQLQuery query = session.createSQLQuery(sql.toString());
			if (where != null) {
				int index = 0;
				for (int i = 0; i < where[1].length; i++) {
					if (where[1][i].getClass().isArray()) {
						for (Object o : (Object[])where[1][i]) {
							query.setParameter(index++, o);
						}
					} else {
						query.setParameter(index++, where[1][i]);
					}
				}
			}
			lis = query.addEntity(clazz).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return lis;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param where	  (where条件，如"字段名,字段名"，多个用逗号隔开)
	 * @param page	  (分页对象)
	 * @param orderStr(条件语句)
	 * @return 对象集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List listData(Class clazz, Map<String, Object> where, ShowPage page, String... orderStr){
		StringBuffer sql = new StringBuffer("SELECT ? FROM " + clazz.getSimpleName().toUpperCase() + " WHERE 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if (where != null) { 
			Iterator<Entry<String, Object>> entryKeyIterator = where.entrySet().iterator();
	        while (entryKeyIterator.hasNext()) {
	            Entry<String, Object> e = entryKeyIterator.next();
	            String key = e.getKey();
	            Object val=e.getValue();
		        if (key.indexOf("BETWEEN") > -1 || key.indexOf("?") > -1) {
					sql.append(" AND " + key);
				} else {
					sql.append(" AND " + key + " LIKE ?");
				}
				if (val.getClass().isArray()) {
					for (Object o : (Object[])val) {
						list.add(o);
					}
				} else {
					list.add(val);
				}
	        }
		}
		StringBuffer sb = new StringBuffer();
		for (String i : orderStr) {
			sb.append(" " + i + " ");
		}
		Session session = HibernateUtil.getSession();
		List<Object> lis = new ArrayList<Object>();
		try {
			SQLQuery query = session.createSQLQuery(sql.toString().replaceFirst("[?]", "*") + sb);
			SQLQuery query2 = session.createSQLQuery(sql.toString().replaceFirst("[?]", "COUNT(1)"));
			int index = 0;
			if (where != null) {
				for (Object val : list) {
					query.setParameter(index, val);
					query2.setParameter(index++, val);
				}
			}
			if (page != null) {
				page.setIntRowCount(Integer.valueOf(query2.uniqueResult().toString()));
				query.setFirstResult(page.getStartRow());
				query.setMaxResults(page.getIntPageSize());
			}
			lis = query.addEntity(clazz).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return lis;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param columns (列名)
	 * @param where	  (key为对应的字段，value为参数值)
	 * @return 集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List listDataByColumn(Class clazz, String columns, Map<String, Object> where){
		StringBuffer sql = new StringBuffer("SELECT ? FROM " + clazz.getSimpleName().toUpperCase() + " WHERE 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if (where != null) {
			Iterator<Entry<String, Object>> entryKeyIterator = where.entrySet().iterator();
	        while (entryKeyIterator.hasNext()) {
	            Entry<String, Object> e = entryKeyIterator.next();
	            String key = e.getKey();
	            Object val=e.getValue();
		        if (key.indexOf("BETWEEN") > -1 || key.indexOf("?") > -1) {
					sql.append(" AND " + key);
				} else {
					sql.append(" AND " + key + " LIKE ?");
				}
				if (val.getClass().isArray()) {
					for (Object o : (Object[])val) {
						list.add(o);
					}
				} else {
					list.add(val);
				}
	        }
		}
		Session session = HibernateUtil.getSession();
		List<Object> lis = new ArrayList<Object>();
		try {
			SQLQuery query = session.createSQLQuery(sql.toString().replaceFirst("[?]", columns));
			int index = 0;
			if (where != null) {
				for (Object val : list) {
					query.setParameter(index++, val);
				}
			}
			lis = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return lis;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param columns (列名)
	 * @param where	  (key为对应的字段，value为参数值)
	 * @return 集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List listDataByColumn(Class clazz, String columns, String joinStr, Map<String, Object> where){
		StringBuffer sql = new StringBuffer("SELECT ? FROM " + clazz.getSimpleName().toUpperCase() + " " + (joinStr != null ? joinStr : "") + " WHERE 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if (where != null) {
			Iterator<Entry<String, Object>> entryKeyIterator = where.entrySet().iterator();
	        while (entryKeyIterator.hasNext()) {
	            Entry<String, Object> e = entryKeyIterator.next();
	            String key = e.getKey();
	            Object val=e.getValue();
		        if (key.indexOf("BETWEEN") > -1 || key.indexOf("?") > -1) {
					sql.append(" AND " + key);
				} else {
					sql.append(" AND " + key + " LIKE ?");
				}
				if (val.getClass().isArray()) {
					for (Object o : (Object[])val) {
						list.add(o);
					}
				} else {
					list.add(val);
				}
	        }
		}
		Session session = HibernateUtil.getSession();
		List<Object> lis = new ArrayList<Object>();
		try {
			SQLQuery query = session.createSQLQuery(sql.toString().replaceFirst("[?]", columns));
			int index = 0;
			if (where != null) {
				for (Object val : list) {
					query.setParameter(index++, val);
				}
			}
			lis = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return lis;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param columns (列名)
	 * @param where	  (key为对应的字段，value为参数值)
	 * @param page	  (分页对象)
	 * @return 集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List listData(Class clazz, String columns, Map<String, Object> where, ShowPage page){
		StringBuffer sql = new StringBuffer("SELECT ? FROM " + clazz.getSimpleName().toUpperCase() + " WHERE 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if (where != null) {
			Iterator<Entry<String, Object>> entryKeyIterator = where.entrySet().iterator();
	        while (entryKeyIterator.hasNext()) {
	            Entry<String, Object> e = entryKeyIterator.next();
	            String key = e.getKey();
	            Object val=e.getValue();
		        if (key.indexOf("BETWEEN") > -1 || key.indexOf("?") > -1) {
					sql.append(" AND " + key);
				} else {
					sql.append(" AND " + key + " LIKE ?");
				}
				if (val.getClass().isArray()) {
					for (Object o : (Object[])val) {
						list.add(o);
					}
				} else {
					list.add(val);
				}
	        }
		}
		Session session = HibernateUtil.getSession();
		List<Object> lis = new ArrayList<Object>();
		try {
			SQLQuery query = session.createSQLQuery(sql.toString().replaceFirst("[?]", columns));
			SQLQuery query2 = session.createSQLQuery(sql.toString().replaceFirst("[?]", "COUNT(1)"));
			int index = 0;
			if (where != null) {
				for (Object val : list) {
					query.setParameter(index, val);
					query2.setParameter(index++, val);
				}
			}
			if (page != null) {
				page.setIntRowCount(Integer.valueOf(query2.uniqueResult().toString()));
				query.setFirstResult(page.getStartRow());
				query.setMaxResults(page.getIntPageSize());
			}
			lis = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return lis;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param columns (列名)
	 * @param joinStr (表关联语句)
	 * @param where	  (key为对应的字段，value为参数值)
	 * @param page	  (分页对象)
	 * @param orderStr(条件语句)
	 * @return 集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List listData(Class clazz, String columns, String joinStr, Map<String, Object> where, ShowPage page, String... orderStr) {
		StringBuffer sql = new StringBuffer("SELECT ? FROM " + clazz.getSimpleName().toUpperCase() + " " + (joinStr != null ? joinStr : "") + " WHERE 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if (where != null) {
			Iterator<Entry<String, Object>> entryKeyIterator = where.entrySet().iterator();
	        while (entryKeyIterator.hasNext()) {
	            Entry<String, Object> e = entryKeyIterator.next();
	            String key = e.getKey();
	            Object val=e.getValue();
		        if (key.indexOf("BETWEEN") > -1 || key.indexOf("?") > -1) {
					sql.append(" AND " + key);
				} else {
					sql.append(" AND " + key + " LIKE ?");
				}
				if (val.getClass().isArray()) {
					for (Object o : (Object[])val) {
						list.add(o);
					}
				} else {
					list.add(val);
				}
	        }
		}
		StringBuffer sb = new StringBuffer();
		for (String i : orderStr) {
			sb.append(" " + i + " ");
		}
		Session session = HibernateUtil.getSession();
		List<Object> lis = new ArrayList<Object>();
		try {
			SQLQuery query = session.createSQLQuery(sql.toString().replaceFirst("[?]", columns != null ? columns : clazz.getSimpleName().toUpperCase() + ".*")+sb);
			SQLQuery query2 = session.createSQLQuery(sql.toString().replaceFirst("[?]", "COUNT(1)"));
			int index = 0;
			if (where != null) {
				for (Object val : list) {
					query.setParameter(index, val);
					query2.setParameter(index++, val);
				}
			}
			if (page != null) {
				page.setIntRowCount(Integer.valueOf(query2.uniqueResult().toString()));
				query.setFirstResult(page.getStartRow());
				query.setMaxResults(page.getIntPageSize());
			}
			lis = (columns != null ? query.list() : query.addEntity(clazz).list());
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return lis;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param where	  (where[0]为对应的字段，where[1]为参数值)
	 * @param orderStr(条件语句)
	 * @return 对象
	 */
	@SuppressWarnings("rawtypes")
	public static Object showData(Class clazz, Object[][] where,String... orderStr){
		if (where != null && where[0].length > 0) {
			StringBuffer sql = new StringBuffer("SELECT * FROM " + clazz.getSimpleName().toUpperCase() + " WHERE 1=1 ");
			for (int i = 0; i < where[0].length; i++) {
				if (where[0][i].toString().indexOf("?") > -1) {
					sql.append(" AND " + where[0][i]);
				} else {
					sql.append(" AND " + where[0][i] + " LIKE ?");
				}
			}
			for (String s : orderStr) {
				sql.append(" " + s + " ");
			}
			Session session = HibernateUtil.getSession();
			try {
				SQLQuery query = session.createSQLQuery(sql.toString());
				int index = 0;
				for (int i = 0; i < where[1].length; i++) {
					if (where[1][i].getClass().isArray()) {
						for (Object o : (Object[])where[1][i]) {
							query.setParameter(index++, o);
						}
					} else {
						query.setParameter(index++, where[1][i]);
					}
				}
				query.setFirstResult(0);
				query.setMaxResults(1);
				List lis = query.addEntity(clazz).list();
				if (lis.size() > 0) {
					return lis.get(0);
				}
			} catch (HibernateException e) {
				e.printStackTrace();
			} finally {
				HibernateUtil.closeSession(session);
			}
		} 
		return null;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @return	行数
	 */
	@SuppressWarnings("rawtypes")
	public static int countData(Class clazz){
		String sql = "SELECT COUNT(1) FROM " + clazz.getSimpleName().toUpperCase();
		Session session = HibernateUtil.getSession();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			return Integer.valueOf(query.uniqueResult().toString());
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return 0;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param where	  (where[0]为对应的字段，where[1]为参数值)
	 * @param orderStr(条件语句)
	 * @return	行数
	 */
	@SuppressWarnings("rawtypes")
	public static int countData(Session session, Class clazz, Object[][] where, String... orderStr){
		StringBuffer sql = new StringBuffer("SELECT COUNT(1) FROM " + clazz.getSimpleName().toUpperCase() + "  WHERE 1=1 ");
		if (where != null && where[0].length > 0) {
			for (int i = 0; i < where[0].length; i++) {
				if (where[0][i].toString().indexOf("?") > -1) {
					sql.append(" AND " + where[0][i]);
				} else {
					sql.append(" AND " + where[0][i] + " LIKE ?");
				}
			}
		}
		for (String s : orderStr) {
			sql.append(" " + s + " ");
		}
		SQLQuery query = session.createSQLQuery(sql.toString());
		if (where != null && where[0].length > 0) {
			int index = 0;
			for (int i = 0; i < where[1].length; i++) {
				if (where[1][i].getClass().isArray()) {
					for (Object o : (Object[])where[1][i]) {
						query.setParameter(index++, o);
					}
				} else {
					query.setParameter(index++, where[1][i]);
				}
			}
		}
		return Integer.valueOf(query.uniqueResult().toString());
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param where	  (where[0]为对应的字段，where[1]为参数值)
	 * @param orderStr(条件语句)
	 * @return	行数
	 */
	@SuppressWarnings("rawtypes")
	public static int countData(Class clazz, Object[][] where, String... orderStr){
		StringBuffer sql = new StringBuffer("SELECT COUNT(1) FROM " + clazz.getSimpleName().toUpperCase() + "  WHERE 1=1 ");
		if (where != null && where[0].length > 0) {
			for (int i = 0; i < where[0].length; i++) {
				if (where[0][i].toString().indexOf("?") > -1) {
					sql.append(" AND " + where[0][i]);
				} else {
					sql.append(" AND " + where[0][i] + " LIKE ?");
				}
			}
		} 
		for (String s : orderStr) {
			sql.append(" " + s + " ");
		}
		Session session = HibernateUtil.getSession();
		try {
			SQLQuery query = session.createSQLQuery(sql.toString());
			if (where != null && where[1].length > 0) {
				int index = 0;
				for (int i = 0; i < where[1].length; i++) {
					if (where[1][i].getClass().isArray()) {
						for (Object o : (Object[])where[1][i]) {
							query.setParameter(index++, o);
						}
					} else {
						query.setParameter(index++, where[1][i]);
					}
				}
			}
			return Integer.valueOf(query.uniqueResult().toString());
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return 0;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param where	  (key为对应的字段，value为参数值)
	 * @param orderStr(条件语句)
	 * @return	行数
	 */
	@SuppressWarnings("rawtypes")
	public static int countData(Class clazz, Map<String, Object> where, String... orderStr){
		StringBuffer sql = new StringBuffer("SELECT COUNT(1) FROM " + clazz.getSimpleName().toUpperCase() + "  WHERE 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if (where != null) {
			Iterator<Entry<String, Object>> entryKeyIterator = where.entrySet().iterator();
	        while (entryKeyIterator.hasNext()) {
	            Entry<String, Object> e = entryKeyIterator.next();
	            String key = e.getKey();
	            Object val=e.getValue();
		        if (key.indexOf("BETWEEN") > -1 || key.indexOf("?") > -1) {
					sql.append(" AND " + key);
				} else {
					sql.append(" AND " + key + " LIKE ?");
				}
				if (val.getClass().isArray()) {
					for (Object o : (Object[])val) {
						list.add(o);
					}
				} else {
					list.add(val);
				}
	        }
		}
		for (String s : orderStr) {
			sql.append(" " + s + " ");
		}
		Session session = HibernateUtil.getSession();
		try {
			SQLQuery query = session.createSQLQuery(sql.toString());
			int index = 0;
			if (where != null) {
				for (Object val : list) {
					query.setParameter(index++, val);
				}
			}
			return Integer.valueOf(query.uniqueResult().toString());
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return 0;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param joinStr (表关联语句)
	 * @param where	  (key为对应的字段，value为参数值)
	 * @param orderStr(条件语句)
	 * @return	行数
	 */
	@SuppressWarnings("rawtypes")
	public static int countData(Class clazz, String joinStr, Map<String, Object> where, String... orderStr){
		StringBuffer sql = new StringBuffer("SELECT COUNT(1) FROM " + clazz.getSimpleName().toUpperCase() + " " + (joinStr != null ? joinStr : "") + "  WHERE 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if (where != null) {
			Iterator<Entry<String, Object>> entryKeyIterator = where.entrySet().iterator();
	        while (entryKeyIterator.hasNext()) {
	            Entry<String, Object> e = entryKeyIterator.next();
	            String key = e.getKey();
	            Object val=e.getValue();
		        if (key.indexOf("BETWEEN") > -1 || key.indexOf("?") > -1) {
					sql.append(" AND " + key);
				} else {
					sql.append(" AND " + key + " LIKE ?");
				}
				if (val.getClass().isArray()) {
					for (Object o : (Object[])val) {
						list.add(o);
					}
				} else {
					list.add(val);
				}
	        }
		}
		for (String s : orderStr) {
			sql.append(" " + s + " ");
		}
		Session session = HibernateUtil.getSession();
		try {
			SQLQuery query = session.createSQLQuery(sql.toString());
			int index = 0;
			if (where != null) {
				for (Object val : list) {
					query.setParameter(index++, val);
				}
			}
			return Integer.valueOf(query.uniqueResult().toString());
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return 0;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param joinStr (表关联语句)
	 * @param where	  (where[0]为对应的字段，where[1]为参数值)
	 * @return	行数
	 */
	@SuppressWarnings("rawtypes")
	public static int countData(Class clazz, String joinStr, Object[][] where){
		if (where != null && where[0].length > 0) {
			StringBuffer sql = new StringBuffer("SELECT COUNT(1) FROM " + clazz.getSimpleName().toUpperCase() + " " + joinStr + " WHERE 1=1 ");
			for (int i = 0; i < where[0].length; i++) {
				if (where[0][i].toString().indexOf("?") > -1) {
					sql.append(" AND " + where[0][i]);
				} else {
					sql.append(" AND " + where[0][i] + " LIKE ?");
				}
			}
			Session session = HibernateUtil.getSession();
			try {
				SQLQuery query = session.createSQLQuery(sql.toString());
				int index = 0;
				for (int i = 0; i < where[1].length; i++) {
					if (where[1][i].getClass().isArray()) {
						for (Object o : (Object[])where[1][i]) {
							query.setParameter(index++, o);
						}
					} else {
						query.setParameter(index++, where[1][i]);
					}
				}
				return Integer.valueOf(query.uniqueResult().toString());
			} catch (HibernateException e) {
				e.printStackTrace();
			} finally {
				HibernateUtil.closeSession(session);
			}
		} 
		return 0;
	}
	/**
	 * 查询数据
	 * @param clazz
	 * @param colStr  (查询结果返回列)
	 * @param joinStr (表关联语句)
	 * @param where	  (where[0]为对应的字段，where[1]为参数值)
	 * @return 行数
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object countData(Class clazz, String colStr, String joinStr, Object[][] where){
		if (where != null && where[0].length > 0) {
			StringBuffer sql = new StringBuffer("SELECT COUNT(1)" + (colStr != null ? "," + colStr : "") + " FROM " + clazz.getSimpleName().toUpperCase() + " " + (joinStr != null ? joinStr : "") + " WHERE 1=1 ");
			for (int i = 0; i < where[0].length; i++) {
				if (where[0][i].toString().indexOf("?") > -1) {
					sql.append(" AND " + where[0][i]);
				} else {
					sql.append(" AND " + where[0][i] + " LIKE ?");
				}
			}
			Session session = HibernateUtil.getSession();
			try {
				SQLQuery query = session.createSQLQuery(sql.toString());
				int index = 0;
				for (int i = 0; i < where[1].length; i++) {
					if (where[1][i].getClass().isArray()) {
						for (Object o : (Object[])where[1][i]) {
							query.setParameter(index++, o);
						}
					} else {
						query.setParameter(index++, where[1][i]);
					}
				}
				if (colStr == null) {
					return Integer.valueOf(query.uniqueResult().toString());
				} else {
					List<Object[]> list = query.list();
					if (list.size() > 0) {
						return list.get(0);
					}
				}
			} catch (HibernateException e) {
				e.printStackTrace();
			} finally {
				HibernateUtil.closeSession(session);
			}
		} 
		return colStr == null ? 0 : null;
	}
	/**
	 * 查询数据
	 * @param sql		(SQL语句)
	 * @param where		(参数数组)
	 * @return	行数
	 */
	public static int countData(String sql, Object[] where){
		Session session = HibernateUtil.getSession();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			if (where != null) {
				int index = 0;
				for (Object o : where) {
					query.setParameter(index++, o);
				}
			}
			return Integer.valueOf(query.uniqueResult().toString());
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return 0;
	}
	@SuppressWarnings("unchecked")
	public static Object uniqueResult(String sql, Object[] where){
		Session session = HibernateUtil.getSession();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			if (where != null) {
				int index = 0;
				for (Object o : where) {
					query.setParameter(index++, o);
				}
			}
			List<Object> objList = query.list();
			if (objList.size() > 0) {
				return objList.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static Object uniqueResult(Session session, String sql, Object[] where){
		SQLQuery query = session.createSQLQuery(sql);
		if (where != null) {
			int index = 0;
			for (Object o : where) {
				query.setParameter(index++, o);
			}
		}
		List<Object> objList = query.list();
		if (objList.size() > 0) {
			return objList.get(0);
		}
		return null;
	}
	
	
}
