<!DOCTYPE html>
<html>
	<head>
		<meta charset=utf-8/>
		<title>查看商品</title>
		<link rel="stylesheet" href="../css/style.css">
		<script>  
			function sub(){          
				var a = buy.ans.value;   
				a = parseInt(a);               
				a--;               
				if(a<=0){
					a = 0;   
				}
				buy.ans.value = a;   
			}   
			function add(){   
				var a = buy.ans.value;   
				a = parseInt(a);   
				a++;   
				buy.ans.value = a;   
			}
			function addtocart(){
				if(buy.ans.value<1)return;
				if(confirm("确定添加商品${name}共计"+buy.ans.value+"件到购物车？")){
					alert("添加成功！");
					buy.submit();
				}	
			}
			function modifyitem(){
				if(confirm("修改商品 ${name} ？")){
					window.location.href=("/work/shop/edit?id=${id}");
				}
			}
			function deleteitem(){
				if(confirm("确认删除商品商品 ${name} ？")){
					window.location.href=("/work/shop/delete?id=${id}");
				}
			}
		</script> 
	</head>
	<body>
		<#include "/header.html">
		<div class="g-doc">
	    <div class="n-show f-cb" id="showContent">
	        <div class="cnt">
	            <div class="img">
		             <img src=${picture}	alt=${name}>
		        </div>
		        <h2 class="summary">${name}</h2>
	            <p class="v-introduce">${abs}</p>
	            <div class="price">
	            	<#if bought==true>
	            	<span class="v-unit">您购买时候的价格为：</span>
	            	</#if>
	                <span class="v-unit">¥</span><span class="v-value">${price}</span>
	            </div>
	             <#if type?exists> 
					<#if type=="seller">
	            		<div class="v-introduce">已售出${sold}件。</div>
	            	</#if>
	            </#if>
	            <br />
	            
	            <#if type?exists> 
					<#if type=="buyer">
					<!-- 表单开始 -->
			            <form id="buy" method="post" target="_parent" action="/work/shop/tocart?iid=${id}">
			            	<div class="num">
			            		购买数量：
			            		<input onclick="sub()" type="button" value="-" style="width:25px;"/>
			            		
								<input id="ans" class="modify_num" name="count" type="text" value="1"  readonly="readonly" style="width:25px;" />
								
								<input onclick="add()" type="button" value="+" style="width:25px;"/>
			       			</div>
					    </form>
					 <!-- 表单结束 -->
					 <div class="oprt f-cb">
					 	<#if bought==false>
			            	<button id="addtocart" onclick="addtocart()" class="u-btn u-btn-primary">
			            	加入购物车
			            	</button>
			            <#else>
			            	<button class="u-btn u-btn-disabled">
			            	您已购买此商品
			            	</button>
			            </#if>
			            	
			         </div>	  
					 
			   		<#elseif type=="seller">
					        <div class="oprt f-cb">
			            		<button class="u-btn u-btn-primary" onclick="modifyitem()" >
			            		修改信息
			            		</button>
			            		<#if sold?eval==0>
			            		<button class="u-btn u-btn-primary" onclick="deleteitem()">
			            			删除商品
			            		</button>
			            		</#if>
			            	</div>
			         </#if>
			    </#if>
	    </div>
	    <div class="m-tab m-tab-fw m-tab-simple f-cb">
	        <h2>详细信息</h2>
	    </div>
	    <div class="n-detail">
	        ${introduce}
	    </div>
	</div>
	
	<div class="n-foot">
	    <p>赵延松网易大作业</p>
	</div>
	</body>
</html>
