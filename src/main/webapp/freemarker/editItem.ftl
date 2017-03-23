<#assign pic_type="url">
<!DOCTYPE html>
<html>
	<head>
	    <meta charset=utf-8/>
	    <title>编辑商品</title>
	    <link rel="stylesheet" href="../css/style.css">
	    <script>
	    	function change1(){
	    			image.style.display="";
	    			urlUpload.style.display="";
	    			fileUpload.style.display="none";
	    			pic_type='url';
			}
			function change2(){
					image.style.display="none";
	    			urlUpload.style.display="none";
	    			fileUpload.style.display="";
	    			pic_type='file';
	    	}
	    	function imagesrc(){
	    		image.src = urlvalue.value;
	    	}
	    	function imagePreview(){
	    		var filesize = fileUp.files[0].size/1024;
	    		if(filesize>1024){
	    			alert("文件大小超过限制，请上传1MB之内的图片！");
	    			fileUp.files[0]=null;
	    			submitform.disabled="disabled";
	    			submitform.style.display="none";
	    			return;
	    		}
	    		submitform.disabled="";
	    		submitform.style.display="";
	    	}
	    	function submitForm(){
	    	if (fname.value.length==0){
	    		alert("商品名不能为空！");
	    		return;
	    	}
	    	if (fabs.value.length==0){
	    		alert("请输入简介！");
	    		return;
	    	}
	    	if (fprice.value.length==0){
	    		alert("必须填写商品价格！");
	    		return;
	    	}
	    	if (fintroduce.value.length==0){
	    		fintroduce.value="卖家未定义相关介绍正文";
	    	}
	    	var reg=/^[0-9]{1,5}(.[0-9]{1,2})?$/;
	    	if(!reg.test(fprice.value)){
	    		alert('请输入正确的数值！');
	    		return;
	    	}
	    	if(typeof(pic_type)!='undefined'){
		    	if(pic_type=='file'){
		    		if(fileUp.files[0]==null){
		    			alert('上传图片不能为空！');
		    			return;
		    		}
		    		form.action = form.action+'withimg';
		    	}
		    	else{
		    		if(urlvalue.value.lenght<=1){
		    			alert('请输入图片url！');
		    			return ;
		    		}
		    	}
	    	}
	    	 form.submit();
	    }

	    </script>
	</head>
	<body>
	    <#include "/header.html">
		
            <div class="n-public">
		<form class="m-form m-form-ht" id="form" method="post" autocomplete="off" enctype="multipart/form-data"
		action=<#if modify==false>"/work/shop/insertitem"<#else>"/work/shop/modifyitem"</#if> 
		      autocomplete="off" >
		    <div class="fmitem">
			
			<#if id?exists>
			    <input id="fid" name="iid" value=${id} hidden>
			</#if>
			<input name="pic_type" value=${pic_type} hidden>
			
			<label class="fmlab">标题：</label>
			<div class="fmipt">
			    <input id="fname" id="name" class="u-ipt ipt" name="iname" autofocus placeholder="2-80字符"
			    <#if name?exists> value=${name} </#if>
			    />
			</div>
		    </div>
		    <div class="fmitem">
			<label class="fmlab">摘要：</label>
			<div class="fmipt">
			    <input id="fabs" class="u-ipt ipt" name="abs"
			    <#if abs?exists> value=${abs} </#if>	   
			    placeholder="2-140字符"/> 
			</div>
			</div >
		    <div class="fmitem">
            <label class="fmlab">图片：</label>
            	<div class="fmipt" id="uploadType">
	                <input name="pic" type="radio" value="url" onclick="change1()" checked /> 图片地址
					<input name="pic" type="radio" value="file" onclick="change2()"/> 本地上传
				</div>	
            </div>
            <div class="fmitem">
                <label class="fmlab"></label>
                <div class="fmipt" id="urlUpload">
                    <input id="urlvalue" class="u-ipt ipt"  name="picture" placeholder="图片地址" 
                    onchange="imagesrc()"
                    <#if picture?exists>value=${picture}</#if>
                    />
                </div>
                <div class="fmipt" id="fileUpload" style="display:none;">
                    <input id="fileUp" class="u-ipt ipt" name="file" type="file"  
                    onchange="imagePreview()" accept=".png,.jpg,.bmp"/>
				</div>
            </div>
		    
		    <div class="fmitem">
			<label class="fmlab">正文：</label>
			<div class="fmipt">
			    <textarea class="u-ipt" name="introduce" rows="10" id="fintroduce"
			    placeholder="2-1000个字符"><#if introduce?exists>${introduce}</#if></textarea> 
			</div>
		    </div>
		    <div class="fmitem">
			<label class="fmlab">价格：</label>
			<div class="fmipt">
			    <input class="u-ipt price" name="price" id="fprice"
			    <#if price?exists> value=${price} </#if>   
			    />元
			</div>
		    </div>
		    <div class="fmitem fmitem-nolab fmitem-btn">
		    </br>
		    </div>
		</form>
		<div class="fmipt">
			    <button id="submitform" onclick="submitForm()" class="u-btn u-btn-primary u-btn-lg">
			   	 发 布</button>
		</div>
		<span class="imgpre">
			<img id="image" <#if picture?exists>src=${picture}</#if> />
		</span>
			
		</div>
	    </div>   
            
	    <div class="n-foot">
		<p>赵延松网易大作业</p>
	    </div>
	</body>
</html>