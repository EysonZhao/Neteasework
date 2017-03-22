<!DOCTYPE html>
<html>
	<head>
		<meta charset=utf-8/>
		<title>账务列表</title>
		<link rel="stylesheet" href="../css/style.css">
		
	</head>
	<body>
		<#include "/header.html">
		
		<div id="boughtItemList">
			<table id="boughtItemTable" class="m-table m-table-row n-table g-b3">
	        <colgroup>
	        	<col class="img"/><col/>
	        	<col class="name"/><col/>
	        	<col class="time"/><col/>
	        	<col class="price"/><col/>
	        	<col class="num"/><col/>
	        	<col class="amount"/><col/>
	        </colgroup>
	        <thead>
	            <tr>
	            	<th>图片</th>
	            	<th>名称</th>
	            	<th>时间</th>
	            	<th>单价</th>
	            	<th>数量</th>
	            	<th>总额</th>
	            </tr>
	        </thead>
	        <tbody>
	        	<#list boughtItems as item>
	        		<tr>
	        		<!-- 图片 -->
		        		<td>
		        			<a href="/work/shop/show?id=${item.getIid()}">
		        				<img src=${item.getPicture()} alt=${item.getBiname()}>
		        			</a>
		        		</td>
		        	<!-- 商品名称  -->
		        		<td>
		        			<h4>
		        			<a href="/work/shop/show?id=${item.getIid()}">${item.getBiname()}</a>
		        			</h4>
		        		</td>
		        	<!-- 时间 -->
		        		<td>
		        			<p>${item.getTime()}</a>
		        		</td>
		        	<!-- 单价  -->
		        		<td>
		        			<span class="v-unit">¥</span>
		        			<span>${item.getBprice()}</span>
		        		</td>
		        	<!-- 购物数量 -->
		        		<td>
							<p class="modify_num" >${item.getNum()} </p>
					<!-- 总额 -->
		                <td>
		                	<span class="v-unit">¥</span>
		                	<span>${item.getAmount()}</span>
		                </td>
	        		</tr>
	        	</#list>
	        </tbody>
	        </table>
		</div>
		<div class="n-abovefoot">
					您一共购买了 ${boughtItems?size} 件商品，消费合计：
					<span class="unit">¥</span>
					<span class="value">${sum}</span>
	    </div>
		
		<div class="n-foot">
	    	<p>赵延松网易大作业</p>
		</div>
		
	</body>
</html>