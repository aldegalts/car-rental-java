<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-5">
        <div class="card shadow">
            <div class="card-body">
                <h3 class="card-title text-center mb-4">Регистрация</h3>

                <form action="${pageContext.request.contextPath}/register" method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label">Имя пользователя</label>
                        <input type="text" class="form-control" id="username" name="username"
                               value="${username}" required autofocus>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Пароль</label>
                        <input type="password" class="form-control" id="password" name="password"
                               minlength="4" required>
                    </div>
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Подтверждение пароля</label>
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                               minlength="4" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Зарегистрироваться</button>
                </form>

                <div class="text-center mt-3">
                    <span>Уже есть аккаунт?</span>
                    <a href="${pageContext.request.contextPath}/login">Войти</a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
