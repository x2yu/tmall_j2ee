<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<title>模仿天猫官网</title>
</head>
<body>
	<div class="categoryPictureInProductPageDiv">
		<img class="categoryPictureInProductpage" src="img/category/${p.category.id}.jpg">
	</div>
	<div class="productPageDiv">
		<%@include file="imgAndInfo.jsp" %>
		<%@include file="productReview.jsp" %>
		<%@include file="productDetail.jsp" %>
	</div>
</body>
</html>