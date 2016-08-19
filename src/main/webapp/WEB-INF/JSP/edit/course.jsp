<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="resume" tagdir="/WEB-INF/tags"%>
<resume:edit-navtab section="Course" />
<form:form action="/edit/course" method="post" id="form" commandName="form">
	<div class="container resume-edit-block">
		<h2 class="text-center">Courses</h2>
		<hr/>
		<c:set var="size" value="${form.items.size()}" />
		<table class="table table-borderless" id="editTable" data-init-size="${size}">
			<c:forEach var="course" items="${form.items}" varStatus="status">
				<resume:edit-block-course index="${status.index}" course="${course}" minYear="${minYear}" maxYear="${maxYear}" />
			</c:forEach>
			<resume:edit-block-course index="${size}" minYear="${minYear}" maxYear="${maxYear}" />
			<tr id="addItemRow">
				<td class="align-center">
					<hr/>
					<button id="addItem" type="button" class="btn btn-default">Add record</button>
					<button id="submitListBtn" type="button" class="btn btn-default">Save changes</button>
				</td>
			</tr>
		</table>
	</div>
</form:form>