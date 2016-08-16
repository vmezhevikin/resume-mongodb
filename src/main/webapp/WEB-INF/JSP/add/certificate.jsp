<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="resume" tagdir="/WEB-INF/tags"%>
<resume:edit-navtab section="Certificate" />
<form:form action="/add/certificate?${_csrf.parameterName}=${_csrf.token}" method="post" commandName="form" enctype="multipart/form-data">
	<div class="container resume-edit-block">
		<h2 class="text-center">Certificates</h2>
		<table class="table">
			<tr>
				<td>
					<div class="form-group">
						<label>Certificate image</label>
						<div class="input-group">
							<span class="form-control" id="fileName">Choose certificate image file</span>
							<span class="input-group-btn">
								<input class="input-file" id="fileInput" name="file" type="file" />
								<span class="btn btn-warning" id="fileButton">
									<i class="fa fa-folder-open" aria-hidden="true"></i>
									Browse...
								</span>
							</span>
						</div>
						<form:errors path="file" cssClass="alert alert-danger" role="alert" element="div" />
					</div>
					<div class="form-group">
						<label>Description</label>
						<input name="description" type="text" class="form-control" placeholder="Description" value="${form.description}" />
						<form:errors path="description" cssClass="alert alert-danger" role="alert" element="div" />
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="3" class="align-center">
					<button type="submit" class="btn btn-primary">Add</button>
					<a href="/edit/certificate" class="btn btn-danger">Back</a>
				</td>
			</tr>
		</table>
	</div>
</form:form>