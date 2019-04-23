package com.chen.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.ContextLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * BeanUtils的等价类，只是将check exception改为uncheck exception
 * @author badqiu
 */
public class BeanUtils
{
	private static Logger logger=LoggerFactory.getLogger(BeanUtils.class);
	
	/**
	 * 可以用于判断 Map,Collection,String,Array,Long是否为空
	 * @param o java.lang.Object.
	 * @return boolean.
	 */
	@SuppressWarnings("unused")
	public static boolean isEmpty(Object o) 
	{
		if (o == null) return true;
		if (o instanceof String){
			if (((String) o).trim().length() == 0){
				return true;
			}
		}
		else if (o instanceof Collection){
			if (((Collection) o).isEmpty()){
				return true;
			}
		}
		else if (o.getClass().isArray()){
			if (((Object[]) o).length == 0){
				return true;
			}
		}
		else if (o instanceof Map){
			if (((Map) o).isEmpty()){
				return true;
			}
		}
		
		
		return false;
	
	}
	
	/**
	 * 判断对象是否为空。
	 * o:为null 返沪true。
	 * 如果为集合，判断集合大小是否为0 为0则返回true
	 * Double，Float,Long,Short,Integer 为0也表示为空。
	 * @param o
	 * @return
	 */
	public static boolean isZeroEmpty(Object o) 
	{
		if (o == null) return true;
		if (o instanceof String){
			if (((String) o).trim().length() == 0){
				return true;
			}
		}
		else if (o instanceof Collection){
			if (((Collection) o).isEmpty()){
				return true;
			}
		}
		else if (o.getClass().isArray()){
			if (((Object[]) o).length == 0){
				return true;
			}
		}
		else if (o instanceof Map){
			if (((Map) o).isEmpty()){
				return true;
			}
		}
		else if (o instanceof Double){
			Double lEmpty=0.0;
			if(o==lEmpty){
				return true;
			}
		}
		else if (o instanceof Float){
			Float lEmpty=0f;
			if(o==lEmpty){
				return true;
			}
		}
		else if (o instanceof Long){
			Long lEmpty=0L;
			if(o==lEmpty){
				return true;
			}
		}
		else if (o instanceof Short){
			Short sEmpty=0;
			if(o==sEmpty ){
				return true;
			}
		}
		else if (o instanceof Integer){
			Integer sEmpty=0;
			if(o==sEmpty ){
				return true;
			}
		}
		
		return false;
	
	}
	

	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * @param o
	 * @return
	 */
	public static boolean isNotEmpty(Object o)
	{
		return !isEmpty(o);
	}
	
	public static boolean isNotEmpty(Long o)
	{
		return !isEmpty(o);
	}
	
	/**
	 * 判断对象不为空。
	 * 如果为0表示为空。
	 * @param o
	 * @return
	 */
	public static boolean isNotIncZeroEmpty(Object o)
	{
		return !isZeroEmpty(o);
	}
	

	/**
	 * 判断是否为数字
	 * @param o
	 * @return
	 */
	public static boolean isNumber(Object o)
	{
		if (o == null) 	return false;
		
		if (o instanceof Number){
			return true;
		}
		if (o instanceof String){
			try{
				Double.parseDouble((String) o);
				return true;
			}
			catch (NumberFormatException e){
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 根据指定的类名判定指定的类是否存在。
	 * @param className
	 * @return
	 */
	public static boolean validClass(String className){
		try{
			Class.forName(className);
			return true;
		}
		catch (ClassNotFoundException e)
		{
			 return false;
		}
	}
	
	/**
	 * 判定类是否继承自父类
	 * @param cls	子类
	 * @param parentClass	父类
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean isInherit(Class cls,Class parentClass)
	{
		return parentClass.isAssignableFrom(cls);
	}

	/**
	 * 获取bean
	 * @param cls
	 * @return
	 */
	public  static Object getBean(Class cls)
	{
		ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		
		return ctx.getBean(cls);
	}
	
	
	/**
	 * 输入基类包名，扫描其下的类，返回类的全路径
	 * @param basePackages 如：com.suneee
	 * @return
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("all")
	public static List<String> scanPackages(String basePackages) throws IllegalArgumentException{
		
		ResourcePatternResolver rl = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(rl);
		List result = new ArrayList();
		String[] arrayPackages = basePackages.split(",");
		try {
			for(int j = 0; j < arrayPackages.length; j++) {
				String packageToScan = arrayPackages[j];
				String packagePart = packageToScan.replace('.', '/');
				String classPattern = "classpath*:/" + packagePart + "/**/*.class";
				Resource[] resources = rl.getResources(classPattern);
				for (int i = 0; i < resources.length; i++) {
					Resource resource = resources[i];
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
					String className = metadataReader.getClassMetadata().getClassName();
					result.add(className);
				}
			}
		}catch(Exception e) {
			new IllegalArgumentException("scan pakcage class error,pakcages:"+basePackages);
		}

		return result;
	}


	private static void handleReflectionException(Exception e)
	{
		ReflectionUtils.handleReflectionException(e);
	}
	
	
	/**
	 * 将字符串数据按照指定的类型进行转换。
	 * 
	 * @param typeName	实际的数据类型
	 * @param value		字符串值。
	 * @return  Object
	 */
	public static Object convertByActType(String typeName,String value){
		Object o = null;
		if (typeName.equals("int")) {
			o = Integer.parseInt(value);
		} else if (typeName.equals("short")) {
			o = Short.parseShort(value);
		} else if (typeName.equals("long")) {
			o = Long.parseLong(value);
		} else if (typeName.equals("float")) {
			o = Float.parseFloat(value);
		} else if (typeName.equals("double")) {
			o = Double.parseDouble(value);
		} else if (typeName.equals("boolean")) {
			o = Boolean.parseBoolean(value);
		} else if (typeName.equals("java.lang.String")) {
			o = value;
		}
		else{
			o=value;
		}
		return o;
	}
	
	
	
}
