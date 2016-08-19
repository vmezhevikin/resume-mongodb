<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="resume" tagdir="/WEB-INF/tags"%>
<resume:edit-navtab section="Experience" />
<form:form action="/edit/experience" method="post" id="form" commandName="form">
	<div class="container resume-edit-block">
		<h2 class="text-center">Practical experience</h2>
		<hr/>
		<c:set var="itemsSize" value="${form.items.size()}" />
		<table class="table table-borderless" id="editTable" data-init-size="${itemsSize}">
			<c:forEach var="experience" items="${form.items}" varStatus="status">
				<resume:edit-block-experience index="${status.index}" experience="${experience}" minYear="${minYear}" maxYear="${maxYear}"/>
			</c:forEach>
			<resume:edit-block-experience index="${itemsSize}" minYear="${minYear}" maxYear="${maxYear}"/>
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