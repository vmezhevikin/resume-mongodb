<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="resume" tagdir="/WEB-INF/tags"%>
<resume:edit-navtab section="Skill" />
<form:form action="/edit/skill" method="post" id="form" commandName="form">
	<div class="container resume-edit-block">
		<h2 class="text-center">Technical skills in the frameworks and technologies.</h2>
		<hr/>
		<c:set var="itemsSize" value="${form.items.size()}" />
		<table class="table table-borderless" id="editTable" data-init-size="${itemsSize}">
			<tr>
				<th>Category</th>
				<th>Description</th>
			</tr>
			<c:forEach var="skill" items="${form.getItems()}" varStatus="status">
				<resume:edit-block-skill index="${status.index}" skill="${skill}" />
			</c:forEach>
			<resume:edit-block-skill index="${itemsSize}" />
			<tr id="addItemRow">
				<td colspan="3" class="align-center">
					<hr/>
					<button id="addItem" type="button" class="btn btn-success">Add</button>
					<button id="submitListBtn" type="button" class="btn btn-primary">Edit</button>
				</td>
			</tr>
		</table>
	</div>
</form:form>