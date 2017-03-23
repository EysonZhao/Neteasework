<!DOCTYPE html>
<html>
	<head>
		<meta charset=utf-8/>
		<title>购物车</title>
		<link rel="stylesheet" href="../css/style.css">
		<!--  以下javascript脚本用来调整购物车中的物品的数量  -->
		<script>  
			function sub(){          
				var a = buy.ans.value;   
				a = parseInt(a);               
				a--;               
				if(a<=0){
					a = 0;   
				}
				cartItemList.tbody. = a;   
			}   
			function add(){   
				var a = buy.ans.value;   
				a = parseInt(a);   
				a++;
				buy.ans.value = a;   
			}   
		</script>
		
	</head>
	<body>
		<#include "/header.html">
		
		<div id="cartItemList">
			<table id="cartItemList" class="m-table m-table-row n-table g-b3">
	        <colgroup>
	        	<col class="img"/><col/>
	        	<col class="name"/><col/>
	        	<col class="num"/><col/>
	        	<col class="price"/><col/>
	        </colgroup>
	        <thead>
	            <tr>
	            	<th>图片</th>
	            	<th>名称</th>
	            	<th>单价</th>
	            	<th>数量</th>
	            	<th>总额</th>
	            	<th>操作</th>
	            </tr>
	        </thead>
	        <tbody>
	        	<#list cartItems as item>
	        		<tr>
	        		<!-- 图片 -->
		        		<td>
		        			<a href="/work/shop/show?id=${item.getIid()}">
		        				<img src=${item.getPicture()} alt=${item.getIname()}>
		        			</a>
		        		</td>
		        	<!-- 商品名称  -->
		        		<td>
		        			<h4>
		        			<a href="/work/shop/show?id=${item.getIid()}">${item.getIname()}</a>
		        			</h4>
		        		</td>
		        	<!-- 单价  -->
		        		<td>
		        			<span class="v-unit">¥</span>
		        			<span>${item.getPrice()}</span>
		        		</td>
		        	<!-- 购物数量 -->
		        		<td>
		        		<!-- 这里想做成可调节的形式了，作业里没要求，而且实现起来暂时没思路，先注释掉，搁置如此 -->
		        	   <!-- <input onclick="sub()" type="button" value="-" width:"25px"/> -->
		        			
							<p class="modify_num" >${item.getCount()} </p>
							
					   <!-- <input onclick="add()" type="button" value="+" width:"25px"/> -->
							
					<!-- 总额 -->
		                <td>
		                	<span class="v-unit">¥</span>
		                	<span>${item.getAmount()}</span>
		                </td>
		           	<!-- 删除 -->
		                <td>
		                	<a href="/work/shop/deletecart?id=${item.getIid()}">
		                	删除</a>
		                </td>
	        		</tr>
	        	</#list>
	        </tbody>
	        </table>
		</div>
		 
		 <div id="act-btn">
			 <button class="u-btn u-btn-primary" id="back"
			 	onclick="javascript:history.back(-1);">
			 	退出
			 </button>
	 		 <button class="u-btn u-btn-primary" id="account" onclick=
	 		 	"javascript:if(confirm('确认购买购物车中全部商品吗？')){window.location.href='/work/shop/buy';}">
	 		 购买</button>
 		 </div>
		
		<div class="n-foot">
	    	<p>赵延松网易大作业</p>
		</div>
		
	</body>
</html>