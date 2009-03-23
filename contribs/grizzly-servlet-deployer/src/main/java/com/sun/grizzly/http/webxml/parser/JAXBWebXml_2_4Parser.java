package com.sun.grizzly.http.webxml.parser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import com.sun.grizzly.http.webxml.schema.version_2_4.AuthConstraintType;
import com.sun.grizzly.http.webxml.schema.version_2_4.DescriptionType;
import com.sun.grizzly.http.webxml.schema.version_2_4.DispatcherType;
import com.sun.grizzly.http.webxml.schema.version_2_4.DisplayNameType;
import com.sun.grizzly.http.webxml.schema.version_2_4.EjbLocalRefType;
import com.sun.grizzly.http.webxml.schema.version_2_4.EjbRefType;
import com.sun.grizzly.http.webxml.schema.version_2_4.EnvEntryType;
import com.sun.grizzly.http.webxml.schema.version_2_4.ErrorPageType;
import com.sun.grizzly.http.webxml.schema.version_2_4.FilterMappingType;
import com.sun.grizzly.http.webxml.schema.version_2_4.FilterType;
import com.sun.grizzly.http.webxml.schema.version_2_4.HttpMethodType;
import com.sun.grizzly.http.webxml.schema.version_2_4.IconType;
import com.sun.grizzly.http.webxml.schema.version_2_4.ListenerType;
import com.sun.grizzly.http.webxml.schema.version_2_4.LoginConfigType;
import com.sun.grizzly.http.webxml.schema.version_2_4.MimeMappingType;
import com.sun.grizzly.http.webxml.schema.version_2_4.ParamValueType;
import com.sun.grizzly.http.webxml.schema.version_2_4.ResourceEnvRefType;
import com.sun.grizzly.http.webxml.schema.version_2_4.ResourceRefType;
import com.sun.grizzly.http.webxml.schema.version_2_4.RoleNameType;
import com.sun.grizzly.http.webxml.schema.version_2_4.SecurityConstraintType;
import com.sun.grizzly.http.webxml.schema.version_2_4.SecurityRoleRefType;
import com.sun.grizzly.http.webxml.schema.version_2_4.SecurityRoleType;
import com.sun.grizzly.http.webxml.schema.version_2_4.ServletMappingType;
import com.sun.grizzly.http.webxml.schema.version_2_4.ServletType;
import com.sun.grizzly.http.webxml.schema.version_2_4.SessionConfigType;
import com.sun.grizzly.http.webxml.schema.version_2_4.TaglibType;
import com.sun.grizzly.http.webxml.schema.version_2_4.UrlPatternType;
import com.sun.grizzly.http.webxml.schema.version_2_4.WebAppType;
import com.sun.grizzly.http.webxml.schema.version_2_4.WebResourceCollectionType;
import com.sun.grizzly.http.webxml.schema.version_2_4.WelcomeFileListType;



public class JAXBWebXml_2_4Parser implements IJAXBWebXmlParser {
	
	Map<String, List<Object>> itemMap = new HashMap<String, List<Object>>();
	
	@SuppressWarnings("unchecked")
	public com.sun.grizzly.http.webxml.schema.WebApp parse(String webxml) throws Exception {
		
		JAXBContext jc = JAXBContext.newInstance("com.sun.grizzly.http.webxml.schema.version_2_4");
        
        // create an Unmarshaller
        Unmarshaller u = jc.createUnmarshaller();
        
        JAXBElement root = (JAXBElement) u.unmarshal(new FileInputStream(webxml));
        
        com.sun.grizzly.http.webxml.schema.WebApp webApp = populate((WebAppType)root.getValue());
        
        return webApp;
	}
	
	private List<com.sun.grizzly.http.webxml.schema.Servlet> populateServlet(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("ServletType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("ServletType");
		
		List<com.sun.grizzly.http.webxml.schema.Servlet> servletList = new ArrayList<com.sun.grizzly.http.webxml.schema.Servlet>();
		
		for (Object obj : list) {
			
			ServletType servlet = (ServletType)obj;
			
			com.sun.grizzly.http.webxml.schema.Servlet servletTmp = new com.sun.grizzly.http.webxml.schema.Servlet();
			
			if(servlet.getIcon()!=null && servlet.getIcon().size()>0){
				servletTmp.setIcon(populateIcon(servlet.getIcon()));
			}
			if(servlet.getDescription()!=null && servlet.getDescription().size()>0){
				servletTmp.setDescription(populateDescription(servlet.getDescription()));
			}
			if(servlet.getDisplayName()!=null && servlet.getDisplayName().size()>0){
				servletTmp.setDisplayName(populateDisplayName(servlet.getDisplayName()));
			}
			if(servlet.getServletName()!=null){
				servletTmp.setServletName(servlet.getServletName().getValue());
			}
			if(servlet.getLoadOnStartup()!=null){
				servletTmp.setLoadOnStartup(servlet.getLoadOnStartup().getValue().toString());
			}
			if(servlet.getJspFile()!=null){
				servletTmp.setJspFile(servlet.getJspFile().getValue());
			}
			if(servlet.getServletClass()!=null){
				servletTmp.setServletClass(servlet.getServletClass().getValue());
			}
			
			List<ParamValueType> initParams = servlet.getInitParam();
			
			if(initParams!=null){
				List<com.sun.grizzly.http.webxml.schema.InitParam> initParamsTmp = new ArrayList<com.sun.grizzly.http.webxml.schema.InitParam>(initParams.size());
				for (ParamValueType initParam : initParams) {
					initParamsTmp.add(getInitParam(initParam));
				}
				
				servletTmp.setInitParam(initParamsTmp);
			}
			
			List<SecurityRoleRefType> securityRoleRefList = servlet.getSecurityRoleRef();
			
			if(securityRoleRefList!=null){
				List<com.sun.grizzly.http.webxml.schema.SecurityRoleRef> securityRoleRefTmpList = new ArrayList<com.sun.grizzly.http.webxml.schema.SecurityRoleRef>(securityRoleRefList.size());
				for (SecurityRoleRefType securityRoleRef : securityRoleRefList) {
					securityRoleRefTmpList.add(getSecurityRoleRef(securityRoleRef));
				}
				
				servletTmp.setSecurityRoleRef(securityRoleRefTmpList);
			}
		
			servletList.add(servletTmp);
		}
		
		return servletList;
	}

	private List<String> populateDescription(List<DescriptionType> list){
		
		if(list==null){
			return null;
		}
		
		List<String> descriptionListTmp = new ArrayList<String>(list.size());
		for (DescriptionType obj : list) {
			DescriptionType item = (DescriptionType)obj;
			descriptionListTmp.add(item.getValue());
		}
		
		return descriptionListTmp;
	}
	
	private List<String> populateDisplayName(List<DisplayNameType> list){
		
		if(list==null){
			return null;
		}
		
		List<String> listTmp = new ArrayList<String>(list.size());
		for (DisplayNameType obj : list) {
			DisplayNameType item = (DisplayNameType)obj;
			listTmp.add(item.getValue());
		}
		
		return listTmp;
	}
	
	protected Map<String, List<Object>> getItemMap(List<Object> itemList) throws Exception {
		// need to find something nicer
		Map<String, List<Object>> itemMap = null;

		if (itemList != null) {
			itemMap = new HashMap<String, List<Object>>();
			// convert it to a Map, will be easier to retrieve values
			for (Object object : itemList) {
				List<Object> list = null;
				String key = object.getClass().getSimpleName();
				if (itemMap.containsKey(key)) {
					list = itemMap.get(key);
				} else {
					list = new ArrayList<Object>();
					itemMap.put(key, list);
				}
				list.add(object);
			}
		} else {
			// error handling when list is null ...
			throw new Exception("invalid");
		}

		return itemMap;
	}
	
	private com.sun.grizzly.http.webxml.schema.WebApp populate(WebAppType root) throws Exception {
		
		com.sun.grizzly.http.webxml.schema.WebApp webApp = new com.sun.grizzly.http.webxml.schema.WebApp();
		
		List<Object> itemList = root.getDescriptionAndDisplayNameAndIcon();
		
		// extract the items from the web.xml
		Map<String, List<Object>> itemMap = getItemMap(itemList);
		
		if (itemMap == null || itemMap.size()==0) {
			throw new Exception("invalid");
		}
		
		// Distributable
		if(itemMap.containsKey("EmptyType")){
			webApp.setDistributable(true);
		}
		
		webApp.setDisplayName(populateDisplayName(itemMap));
		webApp.setDescription(populateDescription(itemMap));
		webApp.setIcon(populateIcon(itemMap));
		
		webApp.setServlet(populateServlet(itemMap));
		webApp.setServletMapping(populateServletMapping(itemMap));
		webApp.setFilter(populateFilter(itemMap));
		webApp.setFilterMapping(populateFilterMapping(itemMap));
		webApp.setContextParam(populateContextParam(itemMap));
		webApp.setEjbLocalRef(populateEjbLocalRef(itemMap));
		webApp.setEjbRef(populateEjbRef(itemMap));
		webApp.setEnvEntry(populateEnvEntry(itemMap));
		webApp.setErrorPage(populateErrorPage(itemMap));
		webApp.setListener(populateListener(itemMap));
		webApp.setLoginConfig(populateLoginConfig(itemMap));
		webApp.setMimeMapping(populateMimeMapping(itemMap));
		webApp.setResourceRef(populateResourceRef(itemMap));
		webApp.setResourceEnvRef(populateResourceEnvRef(itemMap));
		webApp.setSecurityConstraint(populateSecurityConstraint(itemMap));
		webApp.setSecurityRole(populateSecurityRole(itemMap));
		webApp.setSessionConfig(populateSessionConfig(itemMap));
		webApp.setTaglib(populateTaglib(itemMap));
		webApp.setWelcomeFileList(populateWelcomeFileList(itemMap));//
		
		return webApp;
	}
	
	private List<com.sun.grizzly.http.webxml.schema.Icon> populateIcon(List<IconType> list){
		
		if(list==null){
			return null;
		}
		
		List<com.sun.grizzly.http.webxml.schema.Icon> listTmp = new ArrayList<com.sun.grizzly.http.webxml.schema.Icon>(list.size());
		for (IconType obj : list) {
			listTmp.add(getIcon(obj));
		}
		
		return listTmp;
	}
	
	private List<com.sun.grizzly.http.webxml.schema.Icon> populateIcon(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("DescriptionType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("IconType");
		
		if(list==null){
			return null;
		}
		
		List<com.sun.grizzly.http.webxml.schema.Icon> listTmp = new ArrayList<com.sun.grizzly.http.webxml.schema.Icon>(list.size());
		for (Object obj : list) {
			IconType item = (IconType) obj;
			listTmp.add(getIcon(item));
		}
		return listTmp;
	}

	
	private List<String> populateDescription(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("DescriptionType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("DescriptionType");
		
		if(list==null){
			return null;
		}
		
		List<String> listTmp = new ArrayList<String>(list.size());
		for (Object obj : list) {
			DescriptionType item = (DescriptionType) obj;
			listTmp.add(item.getValue());
		}
		return listTmp;
	}
	
	private List<String> populateDisplayName(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("DisplayNameType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("DisplayNameType");
		
		if(list==null){
			return null;
		}
		
		List<String> listTmp = new ArrayList<String>(list.size());
		for (Object obj : list) {
			DisplayNameType item = (DisplayNameType) obj;
			listTmp.add(item.getValue());
		}
		return listTmp;
	}
	private List<com.sun.grizzly.http.webxml.schema.SecurityConstraint> populateSecurityConstraint(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("SecurityConstraintType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("SecurityConstraintType");
		
		List<com.sun.grizzly.http.webxml.schema.SecurityConstraint> securityConstraintList = new ArrayList<com.sun.grizzly.http.webxml.schema.SecurityConstraint>(list.size());
		for (Object obj : list) {
			SecurityConstraintType security = (SecurityConstraintType)obj;
			securityConstraintList.add(getSecurityConstraint(security));
		}
		
		return securityConstraintList;
	}
	
	private com.sun.grizzly.http.webxml.schema.SecurityConstraint getSecurityConstraint(SecurityConstraintType securityConstraint){
		
		if(securityConstraint==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.SecurityConstraint securityConstraintTmp = new com.sun.grizzly.http.webxml.schema.SecurityConstraint();
		
		if(securityConstraint.getAuthConstraint()!=null){
			securityConstraintTmp.setAuthConstraint(getAuthConstraint(securityConstraint.getAuthConstraint()));
		}
		if(securityConstraint.getDisplayName()!=null && securityConstraint.getDisplayName().size()>0){
			securityConstraintTmp.setDisplayName(populateDisplayName(securityConstraint.getDisplayName()));
		}
		if(securityConstraint.getUserDataConstraint()!=null){
			
			com.sun.grizzly.http.webxml.schema.UserDataConstraint userData =  new com.sun.grizzly.http.webxml.schema.UserDataConstraint();
			
			if(securityConstraint.getUserDataConstraint().getDescription()!=null && securityConstraint.getUserDataConstraint().getDescription().size()>0){
				userData.setDescription(populateDescription(securityConstraint.getUserDataConstraint().getDescription()));
			}
			if(securityConstraint.getUserDataConstraint().getTransportGuarantee()!=null){
				userData.setTransportGuarantee(securityConstraint.getUserDataConstraint().getTransportGuarantee().getValue());
			}
			
			securityConstraintTmp.setUserDataConstraint(userData);
		}
		if(securityConstraint.getWebResourceCollection()!=null){
			securityConstraintTmp.setWebResourceCollection(populateWebResourceCollection(securityConstraint.getWebResourceCollection()));
		}
		
		return securityConstraintTmp;
	}
	
	private List<com.sun.grizzly.http.webxml.schema.LoginConfig> populateLoginConfig(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("LoginConfigType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("LoginConfigType");
		
		List<com.sun.grizzly.http.webxml.schema.LoginConfig> loginConfigList = new ArrayList<com.sun.grizzly.http.webxml.schema.LoginConfig>(list.size());
		for (Object obj : list) {
			LoginConfigType config = (LoginConfigType) obj;
			loginConfigList.add(getLoginConfig(config));
		}
		
		return loginConfigList;
	}
	
	private List<com.sun.grizzly.http.webxml.schema.WelcomeFileList> populateWelcomeFileList(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("WelcomeFileListType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("WelcomeFileListType");
		
		List<com.sun.grizzly.http.webxml.schema.WelcomeFileList> welcomeFileList = new ArrayList<com.sun.grizzly.http.webxml.schema.WelcomeFileList>(list.size());
		for (Object obj : list) {
			WelcomeFileListType welcome = (WelcomeFileListType) obj;
			welcomeFileList.add(getWelcomeFileList(welcome));
		}
		
		return welcomeFileList;
	}
	
	private List<com.sun.grizzly.http.webxml.schema.SessionConfig> populateSessionConfig(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("SessionConfigType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("SessionConfigType");
		
		List<com.sun.grizzly.http.webxml.schema.SessionConfig> sessionConfigList = new ArrayList<com.sun.grizzly.http.webxml.schema.SessionConfig>(list.size());
		for (Object obj : list) {
			SessionConfigType config = (SessionConfigType) obj;
			sessionConfigList.add(getSessionConfig(config));
		}
		
		return sessionConfigList;
	}
	
	private List<com.sun.grizzly.http.webxml.schema.FilterMapping> populateFilterMapping(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("FilterMappingType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("FilterMappingType");
		
		List<com.sun.grizzly.http.webxml.schema.FilterMapping> filterMappingList = new ArrayList<com.sun.grizzly.http.webxml.schema.FilterMapping>(list.size());
		for (Object obj : list) {
			FilterMappingType mapping = (FilterMappingType) obj;
			filterMappingList.add(getFilterMapping(mapping));
		}
		
		return filterMappingList;
	}

	private List<com.sun.grizzly.http.webxml.schema.EnvEntry> populateEnvEntry(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("EnvEntryType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("EnvEntryType");
		
		List<com.sun.grizzly.http.webxml.schema.EnvEntry> envEntryList = new ArrayList<com.sun.grizzly.http.webxml.schema.EnvEntry>(list.size());
		for (Object obj : list) {
			EnvEntryType env = (EnvEntryType)obj;
			envEntryList.add(getEnvEntry(env));
		}
		
		return envEntryList;
	}
	
	private List<com.sun.grizzly.http.webxml.schema.EjbLocalRef> populateEjbLocalRef(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("EjbLocalRefType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("EjbLocalRefType");
		
		List<com.sun.grizzly.http.webxml.schema.EjbLocalRef> ejbLocalRefList = new ArrayList<com.sun.grizzly.http.webxml.schema.EjbLocalRef>(list.size());
		for (Object obj : list) {
			EjbLocalRefType ejb = (EjbLocalRefType)obj;
			ejbLocalRefList.add(getEjbLocalRef(ejb));
		}
		
		return ejbLocalRefList;
	}
	
	private com.sun.grizzly.http.webxml.schema.EjbLocalRef getEjbLocalRef(EjbLocalRefType ejb){
		if(ejb==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.EjbLocalRef ejbLocalRefTmp = new com.sun.grizzly.http.webxml.schema.EjbLocalRef();
		
		if(ejb.getEjbLink()!=null){
			ejbLocalRefTmp.setEjbLink(ejb.getEjbLink().getValue());
		}
		if(ejb.getEjbRefName()!=null){
			ejbLocalRefTmp.setEjbRefName(ejb.getEjbRefName().getValue());
		}
		if(ejb.getEjbRefType()!=null){
			ejbLocalRefTmp.setEjbRefType(ejb.getEjbRefType().getValue());
		}
		if(ejb.getDescription()!=null && ejb.getDescription().size()>0){
			ejbLocalRefTmp.setDescription(populateDescription(ejb.getDescription()));
		}
		if(ejb.getLocal()!=null){
			ejbLocalRefTmp.setLocal(ejb.getLocal().getValue());
		}
		if(ejb.getLocalHome()!=null){
			ejbLocalRefTmp.setLocalHome(ejb.getLocalHome().getValue());
		}
		
		return ejbLocalRefTmp;
	}
	
	private List<com.sun.grizzly.http.webxml.schema.Listener> populateListener(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("ListenerType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("ListenerType");
		
		List<com.sun.grizzly.http.webxml.schema.Listener> contextParamList = new ArrayList<com.sun.grizzly.http.webxml.schema.Listener>(list.size());
		for (Object obj : list) {
			ListenerType listener = (ListenerType)obj;
			contextParamList.add(getListener(listener));
		}
		
		return contextParamList;
	}
	
private List<com.sun.grizzly.http.webxml.schema.Filter> populateFilter(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("FilterType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("FilterType");
		
		List<com.sun.grizzly.http.webxml.schema.Filter> filterList = new ArrayList<com.sun.grizzly.http.webxml.schema.Filter>(list.size());
		for (Object obj : list) {
			FilterType filter = (FilterType)obj;
			
			com.sun.grizzly.http.webxml.schema.Filter filterTmp = new com.sun.grizzly.http.webxml.schema.Filter();
			
			if(filter.getIcon()!=null && filter.getIcon().size()>0){
				filterTmp.setIcon(populateIcon(filter.getIcon()));
			}
			if(filter.getDescription()!=null && filter.getDescription().size()>0){
				filterTmp.setDescription(populateDescription(filter.getDescription()));
			}
			if(filter.getDisplayName()!=null && filter.getDisplayName().size()>0){
				filterTmp.setDisplayName(populateDisplayName(filter.getDisplayName()));
			}
			if(filter.getFilterName()!=null){
				filterTmp.setFilterName(filter.getFilterName().getValue());
			}
			if(filter.getFilterClass()!=null){
				filterTmp.setFilterClass(filter.getFilterClass().getValue());
			}
			
			List<ParamValueType> initParams = filter.getInitParam();
			
			if(initParams!=null){
				List<com.sun.grizzly.http.webxml.schema.InitParam> initParamsTmp = new ArrayList<com.sun.grizzly.http.webxml.schema.InitParam>(initParams.size());
				for (ParamValueType initParam : initParams) {
					initParamsTmp.add(getInitParam(initParam));
				}
				
				filterTmp.setInitParam(initParamsTmp);
			}
			
			filterList.add(filterTmp);
		}
		
		return filterList;
	}
	
	private com.sun.grizzly.http.webxml.schema.InitParam getInitParam(ParamValueType initParam){
	
	if(initParam==null){
		return null;
	}
	
	com.sun.grizzly.http.webxml.schema.InitParam initParamTmp = new com.sun.grizzly.http.webxml.schema.InitParam();
	
	if(initParam.getParamName()!=null){
		initParamTmp.setParamName(initParam.getParamName().getValue());
	}
	if(initParam.getParamValue()!=null){
		initParamTmp.setParamValue(initParam.getParamValue().getValue());
	}
	if(initParam.getDescription()!=null && initParam.getDescription().size()>0){
		initParamTmp.setDescription(populateDescription(initParam.getDescription()));
	}
	
	return initParamTmp;
}
	
	private com.sun.grizzly.http.webxml.schema.FilterMapping getFilterMapping(FilterMappingType filterMapping){
		
		if(filterMapping==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.FilterMapping filterMappingTmp = new com.sun.grizzly.http.webxml.schema.FilterMapping();
		
		if(filterMapping.getFilterName()!=null){
			filterMappingTmp.setFilterName(filterMapping.getFilterName().getValue());
		}
		if(filterMapping.getServletName()!=null){
			List<String> listTmp = new ArrayList<String>(1);
			listTmp.add(filterMapping.getServletName().getValue());
			
			filterMappingTmp.setServletName(listTmp);
		}
		if(filterMapping.getUrlPattern()!=null){
			List<String> listTmp = new ArrayList<String>(1);
			listTmp.add(filterMapping.getUrlPattern().getValue());
			
			filterMappingTmp.setUrlPattern(listTmp);
		}
		if(filterMapping.getDispatcher()!=null){
			filterMappingTmp.setDispatcher(populateDispatcher(filterMapping.getDispatcher()));
		}
		
		return filterMappingTmp;
	}
	
	private List<String> populateDispatcher(List<DispatcherType> list){
		
		if(list==null){
			return null;
		}
		
		List<String> listTmp = new ArrayList<String>(list.size());
		for (DispatcherType obj : list) {
			listTmp.add(((DispatcherType) obj).getValue());
		}
		
		return listTmp;
	}
	
	private com.sun.grizzly.http.webxml.schema.Listener getListener(ListenerType listener){
		
		if(listener==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.Listener listenerTmp = new com.sun.grizzly.http.webxml.schema.Listener();
		
		if(listener.getListenerClass()!=null){
			listenerTmp.setListenerClass(listener.getListenerClass().getValue());
		}
		if(listener.getIcon()!=null && listener.getIcon().size()>0){
			listenerTmp.setIcon(populateIcon(listener.getIcon()));
		}
		if(listener.getDescription()!=null && listener.getDescription().size()>0){
			listenerTmp.setDescription(populateDescription(listener.getDescription()));
		}
		if(listener.getDisplayName()!=null && listener.getDisplayName().size()>0){
			listenerTmp.setDisplayName(populateDisplayName(listener.getDisplayName()));
		}

		return listenerTmp;
	}
	
	private com.sun.grizzly.http.webxml.schema.SecurityRoleRef getSecurityRoleRef(SecurityRoleRefType securityRoleRef){
		
		if(securityRoleRef==null){
			return null;
		}
		com.sun.grizzly.http.webxml.schema.SecurityRoleRef srf = new com.sun.grizzly.http.webxml.schema.SecurityRoleRef();
		
		if(securityRoleRef.getRoleName()!=null){
			srf.setRoleName(securityRoleRef.getRoleName().getValue());
		}
		if(securityRoleRef.getRoleLink()!=null){
			srf.setRoleLink(securityRoleRef.getRoleLink().getValue());
		}
		if(securityRoleRef.getDescription()!=null && securityRoleRef.getDescription().size()>0){
			srf.setDescription(populateDescription(securityRoleRef.getDescription()));
		}
		
		return srf;
		
	}
	
	private com.sun.grizzly.http.webxml.schema.Icon getIcon(IconType icon){
		
		if(icon==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.Icon iconTmp = new com.sun.grizzly.http.webxml.schema.Icon();
		
		if(icon.getSmallIcon()!=null){
			iconTmp.setSmallIcon(icon.getSmallIcon().getValue());
		}
		if(icon.getLargeIcon()!=null){
			iconTmp.setLargeIcon(icon.getLargeIcon().getValue());
		}
		
		return iconTmp;
		
	}

	private List<com.sun.grizzly.http.webxml.schema.EjbRef> populateEjbRef(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("EjbRefType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("EjbRefType");
		
		List<com.sun.grizzly.http.webxml.schema.EjbRef> ejbRefList = new ArrayList<com.sun.grizzly.http.webxml.schema.EjbRef>(list.size());
		for (Object obj : list) {
			EjbRefType ejb = (EjbRefType)obj;
			ejbRefList.add(getEjbRef(ejb));
		}
		
		return ejbRefList;
	}

	private com.sun.grizzly.http.webxml.schema.EjbRef getEjbRef(EjbRefType ejb){
		if(ejb==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.EjbRef ejbRefTmp = new com.sun.grizzly.http.webxml.schema.EjbRef();
		
		if(ejb.getEjbLink()!=null){
			ejbRefTmp.setEjbLink(ejb.getEjbLink().getValue());
		}
		if(ejb.getEjbRefName()!=null){
			ejbRefTmp.setEjbRefName(ejb.getEjbRefName().getValue());
		}
		if(ejb.getEjbRefType()!=null){
			ejbRefTmp.setEjbRefType(ejb.getEjbRefType().getValue());
		}
		if(ejb.getDescription()!=null && ejb.getDescription().size()>0){
			ejbRefTmp.setDescription(populateDescription(ejb.getDescription()));
		}
		if(ejb.getEjbLink()!=null){
			ejbRefTmp.setEjbLink(ejb.getEjbLink().getValue());
		}
		if(ejb.getHome()!=null){
			ejbRefTmp.setHome(ejb.getHome().getValue());
		}
		if(ejb.getRemote()!=null){
			ejbRefTmp.setRemote(ejb.getRemote().getValue());
		}
		
		return ejbRefTmp;
	}
	
	private com.sun.grizzly.http.webxml.schema.EnvEntry getEnvEntry(EnvEntryType envEntry){
		
		if(envEntry==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.EnvEntry envEntryTmp = new com.sun.grizzly.http.webxml.schema.EnvEntry();
		
		if(envEntry.getEnvEntryName()!=null){
			envEntryTmp.setEnvEntryName(envEntry.getEnvEntryName().getValue());
		}
		if(envEntry.getEnvEntryType()!=null){
			envEntryTmp.setEnvEntryType(envEntry.getEnvEntryType().getValue());
		}
		if(envEntry.getEnvEntryValue()!=null){
			envEntryTmp.setEnvEntryValue(envEntry.getEnvEntryValue().getValue());
		}
		if(envEntry.getDescription()!=null && envEntry.getDescription().size()>0){
			envEntryTmp.setDescription(populateDescription(envEntry.getDescription()));
		}
		
		return envEntryTmp;
	}

	private com.sun.grizzly.http.webxml.schema.ErrorPage getErrorPage(ErrorPageType errorPage){
		
		if(errorPage==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.ErrorPage errorPageTmp = new com.sun.grizzly.http.webxml.schema.ErrorPage();
		
		if(errorPage.getLocation()!=null){
			errorPageTmp.setLocation(errorPage.getLocation().getValue());
		}
		if(errorPage.getErrorCode()!=null){
			errorPageTmp.setErrorCode(errorPage.getErrorCode().getValue().toString());
		}
		if(errorPage.getExceptionType()!=null){
			errorPageTmp.setExceptionType(errorPage.getExceptionType().getValue());
		}
		
		return errorPageTmp;
	}

	private List<com.sun.grizzly.http.webxml.schema.ErrorPage> populateErrorPage(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("ErrorPageType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("ErrorPageType");
		
		List<com.sun.grizzly.http.webxml.schema.ErrorPage> errorPageList = new ArrayList<com.sun.grizzly.http.webxml.schema.ErrorPage>(list.size());
		for (Object obj : list) {
			ErrorPageType page = (ErrorPageType)obj;
			errorPageList.add(getErrorPage(page));
		}
		
		return errorPageList;
	}

	private List<com.sun.grizzly.http.webxml.schema.MimeMapping> populateMimeMapping(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("MimeMappingType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("MimeMappingType");
		
		List<com.sun.grizzly.http.webxml.schema.MimeMapping> mimeMappingList = new ArrayList<com.sun.grizzly.http.webxml.schema.MimeMapping>(list.size());
		for (Object obj : list) {
			MimeMappingType mapping = (MimeMappingType)obj;
			mimeMappingList.add(getMimeMapping(mapping));
		}
		
		return mimeMappingList;
	}

	private com.sun.grizzly.http.webxml.schema.MimeMapping getMimeMapping(MimeMappingType mimeMapping){
		
		if(mimeMapping==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.MimeMapping mimeMappingTmp = new com.sun.grizzly.http.webxml.schema.MimeMapping();
		
		if(mimeMapping.getExtension()!=null){
			mimeMappingTmp.setExtension(mimeMapping.getExtension().getValue());
		}
		if(mimeMapping.getMimeType()!=null){
			mimeMappingTmp.setMimeType(mimeMapping.getMimeType().getValue());
		}
		
		return mimeMappingTmp;
	}

	private List<com.sun.grizzly.http.webxml.schema.ResourceEnvRef> populateResourceEnvRef(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("ResourceEnvRefType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("ResourceEnvRefType");
		
		List<com.sun.grizzly.http.webxml.schema.ResourceEnvRef> resourceEnvRefList = new ArrayList<com.sun.grizzly.http.webxml.schema.ResourceEnvRef>(list.size());
		for (Object obj : list) {
			ResourceEnvRefType res = (ResourceEnvRefType)obj;
			resourceEnvRefList.add(getResourceEnvRef(res));
		}
		
		return resourceEnvRefList;
	}

	private com.sun.grizzly.http.webxml.schema.ResourceRef getResourceRef(ResourceRefType resourceRef){
		
		if(resourceRef==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.ResourceRef resourceRefTmp = new com.sun.grizzly.http.webxml.schema.ResourceRef();
		
		if(resourceRef.getResRefName()!=null){
			resourceRefTmp.setResRefName(resourceRef.getResRefName().getValue());
		}
		if(resourceRef.getResAuth()!=null){
			resourceRefTmp.setResAuth(resourceRef.getResAuth().getValue());
		}
		if(resourceRef.getResSharingScope()!=null){
			resourceRefTmp.setResSharingScope(resourceRef.getResSharingScope().getValue());
		}
		if(resourceRef.getResType()!=null){
			resourceRefTmp.setResType(resourceRef.getResType().getValue());
		}
		if(resourceRef.getDescription()!=null && resourceRef.getDescription().size()>0){
			resourceRefTmp.setDescription(populateDescription(resourceRef.getDescription()));
		}
		
		return resourceRefTmp;
	}

	private com.sun.grizzly.http.webxml.schema.AuthConstraint getAuthConstraint(AuthConstraintType authConstraint){
		
		if(authConstraint==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.AuthConstraint authConstraintTmp = new com.sun.grizzly.http.webxml.schema.AuthConstraint();
		
		if(authConstraint.getRoleName()!=null){
			
			List<RoleNameType> list = authConstraint.getRoleName();
			if(list!=null){
				
				List<String> roleList = new ArrayList<String>(list.size());
				for (RoleNameType roleName : list) {
					roleList.add(roleName.getValue());
				}
				
				authConstraintTmp.setRoleName(roleList);
			}
			
		}
	
		if(authConstraint.getDescription()!=null && authConstraint.getDescription().size()>0){
			authConstraintTmp.setDescription(populateDescription(authConstraint.getDescription()));
		}
		
		return authConstraintTmp;
	}

	private List<com.sun.grizzly.http.webxml.schema.ServletMapping> populateServletMapping(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("ServletMappingType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("ServletMappingType");
		
		List<com.sun.grizzly.http.webxml.schema.ServletMapping> servletMappingList = new ArrayList<com.sun.grizzly.http.webxml.schema.ServletMapping>(list.size());
		for (Object obj : list) {
			ServletMappingType mapping = (ServletMappingType)obj;
			servletMappingList.add(getServletMapping(mapping));
		}
		
		return servletMappingList;
	}

	private com.sun.grizzly.http.webxml.schema.WebResourceCollection getWebResourceCollection(WebResourceCollectionType webResourceCollection){
		
		if(webResourceCollection==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.WebResourceCollection webResourceCollectionTmp = new com.sun.grizzly.http.webxml.schema.WebResourceCollection();
		
		if(webResourceCollection.getHttpMethod()!=null){
			List<HttpMethodType> list = webResourceCollection.getHttpMethod();
			if(list!=null){
				
				List<String> httpMethodList = new ArrayList<String>(list.size());
				for (HttpMethodType http : list) {
					httpMethodList.add(http.getValue());
				}
				
				webResourceCollectionTmp.setHttpMethod(httpMethodList);
			}
		}
		if(webResourceCollection.getUrlPattern()!=null){
			List<UrlPatternType> list = webResourceCollection.getUrlPattern();
			if(list!=null){
				
				List<String> urlPatternList = new ArrayList<String>(list.size());
				for (UrlPatternType url : list) {
					urlPatternList.add(url.getValue());
				}
				
				webResourceCollectionTmp.setUrlPattern(urlPatternList);
			}
		}
		if(webResourceCollection.getDescription()!=null && webResourceCollection.getDescription().size()>0){
			webResourceCollectionTmp.setDescription(populateDescription(webResourceCollection.getDescription()));
		}
		if(webResourceCollection.getWebResourceName()!=null){
			webResourceCollectionTmp.setWebResourceName(webResourceCollection.getWebResourceName().getValue());
		}
		
		return webResourceCollectionTmp;
	}

	private com.sun.grizzly.http.webxml.schema.SessionConfig getSessionConfig(SessionConfigType sessionConfig){
		
		if(sessionConfig==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.SessionConfig sessionConfigTmp = new com.sun.grizzly.http.webxml.schema.SessionConfig();
		
		if(sessionConfig.getSessionTimeout()!=null){
			sessionConfigTmp.setSessionTimeout(sessionConfig.getSessionTimeout().getValue().toString());
		}
		
		return sessionConfigTmp;
	}
	
	private com.sun.grizzly.http.webxml.schema.ServletMapping getServletMapping(ServletMappingType servletMapping){
		
		if(servletMapping==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.ServletMapping servletMappingTmp = new com.sun.grizzly.http.webxml.schema.ServletMapping();
		
		if(servletMapping.getServletName()!=null){
			servletMappingTmp.setServletName(servletMapping.getServletName().getValue());
		}
		if(servletMapping.getUrlPattern()!=null){
			List<String> listTmp = new ArrayList<String>(1);
			listTmp.add(servletMapping.getUrlPattern().getValue());
			servletMappingTmp.setUrlPattern(listTmp);
		}
		
		return servletMappingTmp;
	}
	
	private List<com.sun.grizzly.http.webxml.schema.Taglib> populateTaglib(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("TaglibType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("TaglibType");
		
		List<com.sun.grizzly.http.webxml.schema.Taglib> taglibList = new ArrayList<com.sun.grizzly.http.webxml.schema.Taglib>(list.size());
		for (Object obj : list) {
			TaglibType taglib = (TaglibType)obj;
			taglibList.add(getTaglib(taglib));
		}
		
		return taglibList;
	}

	private List<com.sun.grizzly.http.webxml.schema.WebResourceCollection> populateWebResourceCollection(List<WebResourceCollectionType> webResourceCollectionList){

		if(webResourceCollectionList==null){
			return null;
		}
		
		List<com.sun.grizzly.http.webxml.schema.WebResourceCollection> webResourceCollectionListTmp = new ArrayList<com.sun.grizzly.http.webxml.schema.WebResourceCollection>(webResourceCollectionList.size());
		for (WebResourceCollectionType res : webResourceCollectionList) {
			webResourceCollectionListTmp.add(getWebResourceCollection(res));
		}
		
		return webResourceCollectionListTmp;
	}

	private com.sun.grizzly.http.webxml.schema.SecurityRole getSecurityRole(SecurityRoleType securityRole){
		
		if(securityRole==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.SecurityRole securityRoleTmp = new com.sun.grizzly.http.webxml.schema.SecurityRole();
		
		if(securityRole.getRoleName()!=null){
			securityRoleTmp.setRoleName(securityRole.getRoleName().getValue());
		}
		if(securityRole.getDescription()!=null && securityRole.getDescription().size()>0){
			securityRoleTmp.setDescription(populateDescription(securityRole.getDescription()));
		}
		
		return securityRoleTmp;
	}

	private com.sun.grizzly.http.webxml.schema.Taglib getTaglib(TaglibType taglib){
		
		if(taglib==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.Taglib tagLibTmp = new com.sun.grizzly.http.webxml.schema.Taglib();
		
		if(taglib.getTaglibUri()!=null){
			tagLibTmp.setTaglibUri(taglib.getTaglibUri().getValue());
		}
		if(taglib.getTaglibLocation()!=null){
			tagLibTmp.setTaglibLocation(taglib.getTaglibLocation().getValue());
		}
		
		return tagLibTmp;
	}

	private List<com.sun.grizzly.http.webxml.schema.SecurityRole> populateSecurityRole(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("SecurityRoleType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("SecurityRoleType");
		
		List<com.sun.grizzly.http.webxml.schema.SecurityRole> securityRoleList = new ArrayList<com.sun.grizzly.http.webxml.schema.SecurityRole>(list.size());
		for (Object obj : list) {
			SecurityRoleType role = (SecurityRoleType)obj;
			securityRoleList.add(getSecurityRole(role));
		}
		
		return securityRoleList;
	}

	private List<com.sun.grizzly.http.webxml.schema.ResourceRef> populateResourceRef(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("ResourceRefType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("ResourceRefType");
		
		List<com.sun.grizzly.http.webxml.schema.ResourceRef> resourceRefList = new ArrayList<com.sun.grizzly.http.webxml.schema.ResourceRef>(list.size());
		for (Object obj : list) {
			ResourceRefType resource = (ResourceRefType)obj;
			resourceRefList.add(getResourceRef(resource));
		}
		
		return resourceRefList;
	}

	private com.sun.grizzly.http.webxml.schema.LoginConfig getLoginConfig(LoginConfigType loginConfig){
		
		if(loginConfig==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.LoginConfig loginConfigTmp = new com.sun.grizzly.http.webxml.schema.LoginConfig();
		
		if(loginConfig.getAuthMethod()!=null){
			loginConfigTmp.setAuthMethod(loginConfig.getAuthMethod().getValue());
		}
		if(loginConfig.getFormLoginConfig()!=null){
			loginConfigTmp.setFormLoginConfig(new com.sun.grizzly.http.webxml.schema.FormLoginConfig(loginConfig.getFormLoginConfig().getFormLoginPage().getValue(),loginConfig.getFormLoginConfig().getFormErrorPage().getValue()));
		}
		if(loginConfig.getRealmName()!=null){
			loginConfigTmp.setRealmName(loginConfig.getRealmName().getValue());
		}
		
		return loginConfigTmp;
	}
	
	private com.sun.grizzly.http.webxml.schema.WelcomeFileList getWelcomeFileList(WelcomeFileListType welcomeFileList){
		
		if(welcomeFileList==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.WelcomeFileList welcomeFileTmp = new com.sun.grizzly.http.webxml.schema.WelcomeFileList();
		
		if(welcomeFileList.getWelcomeFile()!=null){
			welcomeFileTmp.setWelcomeFile(welcomeFileList.getWelcomeFile());
		}
		
		return welcomeFileTmp;
	}
	
	private List<com.sun.grizzly.http.webxml.schema.ContextParam> populateContextParam(Map<String, List<Object>> itemMap){
		
		if(!itemMap.containsKey("ParamValueType")){
			return null;
		}
		
		List<Object> list = (List<Object>)itemMap.get("ParamValueType");
		
		List<com.sun.grizzly.http.webxml.schema.ContextParam> contextParamList = new ArrayList<com.sun.grizzly.http.webxml.schema.ContextParam>(list.size());
		for (Object obj : list) {
			ParamValueType contextParam = (ParamValueType)obj;
			contextParamList.add(getContextParam(contextParam));
		}
		
		return contextParamList;
	}
	
	private com.sun.grizzly.http.webxml.schema.ResourceEnvRef getResourceEnvRef(ResourceEnvRefType resourceEnvRef){
		
		if(resourceEnvRef==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.ResourceEnvRef resourceEnvRefTmp = new com.sun.grizzly.http.webxml.schema.ResourceEnvRef();
		if(resourceEnvRef.getResourceEnvRefName()!=null){
			resourceEnvRefTmp.setResourceEnvRefName(resourceEnvRef.getResourceEnvRefName().getValue());
		}
		if(resourceEnvRef.getResourceEnvRefType()!=null){
			resourceEnvRefTmp.setResourceEnvRefType(resourceEnvRef.getResourceEnvRefType().getValue());
		}
		if(resourceEnvRef.getDescription()!=null && resourceEnvRef.getDescription().size()>0){
			resourceEnvRefTmp.setDescription(populateDescription(resourceEnvRef.getDescription()));
		}
		
		return resourceEnvRefTmp;
	}

	private com.sun.grizzly.http.webxml.schema.ContextParam getContextParam(ParamValueType contextParam){
		
		if(contextParam==null){
			return null;
		}
		
		com.sun.grizzly.http.webxml.schema.ContextParam contextParamTmp = new com.sun.grizzly.http.webxml.schema.ContextParam();
		
		if(contextParam.getParamName()!=null){
			contextParamTmp.setParamName(contextParam.getParamName().getValue());
		}
		if(contextParam.getParamValue()!=null){
			contextParamTmp.setParamValue(contextParam.getParamValue().getValue());
		}
		if(contextParam.getDescription()!=null && contextParam.getDescription().size()>0){
			contextParamTmp.setDescription(populateDescription(contextParam.getDescription()));
		}
		
		return contextParamTmp;
	}
	
	
	
}
