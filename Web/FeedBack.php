<!DOCTYPE HTML>
<!--
	TXT by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>

<head>
	<title>遠端診療</title>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
	<link rel="stylesheet" href="assets/css/main.css" />
	<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	<link href="//cdn.jsdelivr.net/npm/@sweetalert2/theme-bulma@4/bulma.css" rel="stylesheet">
</head>

<body class="is-preload">
	<div id="page-wrapper">

		<!-- Header -->
		<header id="header">
			<div class="logo container">
				<div>
					<a href="index.php" id="logo">WIOT</a>
					<p>可穿戴式物聯網醫療照護平台</p><br><br>
					<p class="logged-out" style="display: none;">您還未登入，請使用醫院帳戶登入</p>
				</div>

			</div>
		</header>


		<!-- Nav -->
		<?php
		include('nav.php');
		?>

		<!-- Main -->
		<div class="logged-in" style="display: none;">
			<section id="main">
				<div class="container">
					<h2 class="major"><span>意見反饋</span></h2>
					<div class="row">
						<div class="col-3 col-12-medium">
							<div class="sidebar">

								<!-- Sidebar -->

								<!-- Recent Posts -->
								<section>
									<h2 class="major"><span>近期意見反饋</span></h2>
									<ul class="divided" id="feedback-list">

									</ul>
								</section>
							</div>
						</div>
						<div class="col-9 col-12-medium imp-medium">
							<div class="content">

								<!-- Content -->

								<article class="box page-content">

									<header>
										<form id="feedbackForm">
											使用本系統時間：
											<select id="usetime">
												<option>不到一個月</option>
												<option>一個月~半年</option>
												<option>半年~一年</option>
												<option>一年以上</option>
											</select>
											<span class="align_top">想對本系統說的：</span><br>
											<textarea id="say" rows="2" cols="20"></textarea>
											<span class="align_top">本系統需改進的地方：</span><br>
											<textarea id="improve" rows="2" cols="20"></textarea><br>
											<button id="storedata"> 送出</button>

										</form>
									</header>
								</article>

							</div>
						</div>

					</div>
					<div class="row gtr-200">

						<div class="col-12">

							<!-- Features -->
							<section class="box features">
								<h2 class="major"><span>最新消息</span></h2>
								<div class="">
									<div class="row guides">
										<div class="col-3 col-6-medium col-12-small ">

											<!-- Feature -->
											<section class="box feature">
												<a href="#" class="image featured"><img src="images/Arm-IOT2.jpeg" alt="" /></a>
												<h4><a href="#">什麼是物聯網?</a></h4>
												<p>
													物聯網將現實世界數位化，應用範圍十分廣泛。物聯網可拉近分散的資料，統整物與物的數位資訊。
													而我們的物聯網架構可實現在：<strong>健康醫療</strong>領域。
												</p>
											</section>

										</div>
										<div class="col-3 col-6-medium col-12-small">

											<!-- Feature -->
											<section class="box feature">
												<a href="#" class="image featured"><img src="images/Arduino_logo.png" alt="" /></a>
												<h4><a href="#">我們的硬體</a></h4>
												<p>
													硬體方面採用了開發版arduino，可連接不同的感應器有效偵測人體變化的數據，
													目前將可完整偵測人體頸椎角度，之後將可連接更多不同的感應器，進而達成整套的可穿戴事務聯網醫療系統。
												</p>
											</section>

										</div>
										<div class="col-3 col-6-medium col-12-small">

											<!-- Feature -->
											<section class="box feature">
												<a href="#" class="image featured"><img src="images/old-APP.jpg" alt="" /></a>
												<h4><a href="#">可行動的<br>照護系統</a></h4>
												<p>
													在手機上下載我們的app，將可透過網路和藍芽等方式連接感測器，將感測數據回傳到伺服器，並顯示到手機上
													隨時為您身體數據的變化把關，時時刻刻提醒您如何讓自己維持在最佳狀態
												</p>
											</section>

										</div>
										<div class="col-3 col-6-medium col-12-small">

											<!-- Feature -->
											<section class="box feature">
												<a href="#" class="image featured"><img src="images/IMG_20210305_155351.jpg" alt="" /></a>
												<h4><a href="#">開發日誌</a></h4>
												<p>

												</p>
											</section>
										</div>
									</div>
								</div>
							</section>
						</div>
					</div>
				</div>
			</section>
		</div>
	</div>

	<!-- Footer -->
	<footer id="footer">
		<div class="container">
			<div class="row gtr-200">
				<div class="col-12">

					<!-- About -->


				</div>
				<div class="col-12">

					<!-- Contact -->
					<section>
						<h2 class="major"><span>Get in touch</span></h2>
						<ul class="contact">
							<li><a class="icon brands fa-facebook-f" href="#"><span class="label">Facebook</span></a></li>
							<li><a class="icon brands fa-twitter" href="#"><span class="label">Twitter</span></a></li>
							<li><a class="icon brands fa-instagram" href="#"><span class="label">Instagram</span></a></li>
							<li><a class="icon brands fa-dribbble" href="#"><span class="label">Dribbble</span></a></li>
							<li><a class="icon brands fa-linkedin-in" href="#"><span class="label">LinkedIn</span></a></li>
						</ul>
					</section>

				</div>
			</div>

			<!-- Copyright -->
			<div id="copyright">
				<ul class="menu">
					<li>&copy; Untitled. All rights reserved</li>
					<li>Design: <a href="http://html5up.net">HTML5 UP</a></li>
				</ul>
			</div>

		</div>
	</footer>

	</div>

	<!-- Scripts -->
	<script src="https://www.gstatic.com/firebasejs/5.6.0/firebase-app.js"></script>
	<script src="https://www.gstatic.com/firebasejs/5.6.0/firebase-auth.js"></script>
	<script src="https://www.gstatic.com/firebasejs/5.6.0/firebase-firestore.js"></script>
	<script src="https://www.gstatic.com/firebasejs/5.6.0/firebase-functions.js"></script>
	<script>
		// Initialize Firebase
		var config = {
			apiKey: "AIzaSyAXsHdrvFplnL6QrMZn6LTQ2Zv2WvgPNfc",
			authDomain: "vertebracorrectsystem.firebaseapp.com",
			databaseURL: "https://vertebracorrectsystem-default-rtdb.firebaseio.com",
			projectId: "vertebracorrectsystem"
		};
		firebase.initializeApp(config);

		// make auth and firestore references
		const auth = firebase.auth();
		const db = firebase.firestore();
		const functions = firebase.functions();

		// update firestore settings
		db.settings({
			timestampsInSnapshots: true
		});
	</script>

	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/jquery.dropotron.min.js"></script>
	<script src="assets/js/jquery.scrolly.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

	<script src="scripts/auth.js"> </script>
	<script src="scripts/index.js"> </script>
	<script src="scripts/FeedBack.js"></script>

</body>

</html>