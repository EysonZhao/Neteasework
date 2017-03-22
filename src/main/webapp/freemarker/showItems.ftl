<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8"/>
		<title>首页</title>
		<link rel="stylesheet" href="../css/style.css"/>
	</head>
	<body>
		<!-- 这里使用include引入header文件。header文件包括买家/卖家信息和首页选项 -->
		<#include "/header.html">
		
	    <div class="m-tab m-tab-fw m-tab-simple f-cb">
	        <div class="tab">
	            <ul>
	                <li class="z-sel" ><a href="/work/shop/items">所有内容</a></li>
	                <#if type?exists>
		                <#if type=="buyer">
		                	<li class="z-sel" ><a href="/work/shop/itemsunbought">未购买商品</a></li>
		                </#if>
	                </#if>
	                
	            </ul>
	        </div>
	    </div>
	    <div class="n-plist">
	        <ul class="f-cb" id="plist">
	 			 <#if itemlist?? > <!-- 检验1 begin -->
	   				<#list itemlist as item>  
	                	<li id=${item.getId()}>
		                    <a href="/work/shop/show?id=${item.getId()}" class="link">
		                        <div class="img">
		                        	<img src=${item.getPicture()}	alt=${item.getIname()}>
		                        </div>
		                        <h3>${item.getIname()}</h3>
		                        <div class="price">
		                        <span class="v-unit">¥</span>
		                        <span class="v-value"> ${item.getPrice()}</span></div>
		                        </a>
		                        <#if type?exists>
		                        	<#if type=="seller">
		                         	<button class="del" 
			                         	onclick="javascript:if(confirm('确定删除商品 ${item.getIname()}？'))
			                         	{window.location.href='/work/shop/delete?id=${item.getId()}'}">
			                         	删除
		                         	</button>
		                         	</#if>
		                         </#if>
	                	</li>
	  			    </#list>
	              </#if>     <!-- 检验1 end -->
	              <#if itemlist?? > <!-- 检验2 begin -->
	                <#list otheritemlist as item>  
	                	<li id=${item.getId()}>
		                    <a  class="link" 
		                    <#if type=="buyer">
		                    	href="/work/shop/showbought?id=${item.getId()}"
		                    <#else>
		                    	href="/work/shop/show?id=${item.getId()}"
		                    </#if>
		                    >
		                        <div class="img">
		                        	<img src=${item.getPicture()}	alt=${item.getIname()}>
		                        </div>
		                        <h3>${item.getIname()}</h3>
		                        <div class="price">
			                        <span class="v-unit">¥</span>
			                        <span class="v-value"> ${item.getPrice()}</span>
			                 	</div>
			                  <#if type?exists>
		                         	<#if type=="buyer">
		                         		<span class="had"><b>已购买</b></span>
		                         	<#else>
		                         		<span class="had"><b>已售出</b></span>
		                         		<span class="sold"> 售出${item.getSold()}件 </span>
		                         	</#if>
		                         </#if>
	                   		 </a>
	                	</li>
	                </#list>
	                </#if> <!-- 检验2 end -->
	
	        </ul>
	    </div>
	</div>
	
	<div class="n-foot">
	    <p>赵延松网易大作业</p>
	</div>
</body>
</html>