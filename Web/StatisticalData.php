<!DOCTYPE HTML>
<!--
	TXT by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>

<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
	<link rel="stylesheet" href="assets/css/main.css" />
	<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	<link href="//cdn.jsdelivr.net/npm/@sweetalert2/theme-bulma@4/bulma.css" rel="stylesheet">
	<title>資料圖表化系統</title>
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
					<h2 class="major"><span>病患資料搜尋</span></h2>
					<div class="row">

						<div class="col-3 col-12-medium">

							<div class="sidebar">

								<!-- Sidebar -->


								<!-- Recent Posts -->
								<section>
									<h4 class="major"><span>想看的圖表</span></h4>
									<ul class="divided">
										<li>
											<article class="box post-summary">
												<h5><a href="#">折線圖</a></h5>

											</article>
										</li>
										<li>
											<article class="box post-summary">
												<h5><a href="#">長條圖</a></h5>

											</article>
										</li>
										<li>
											<article class="box post-summary">
												<h5><a href="StatisticalData_chart.php">圓餅圖</a></h5>

											</article>
										</li>
									</ul>

								</section>
							</div>
						</div>
						<div class="col-9 col-12-medium imp-medium">
							<div class="content">

								<!-- Content -->

								<article class="box page-content">

							</div>
							<header>
								<div class="input-group input-group-newsletter" style="border-radius:5px">
									<input name="id" class="form-control" type="text" placeholder="請輸入病患id..." aria-label="請輸入病患id..." aria-describedby="submit-button" />
									<button name="submit" class="btn btn-primary" id="submit-button" type="button">搜尋</button>
								</div><br>
							</header>

							<h3>折線圖</h3>

							<head>
								<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
								<script type="text/javascript">
									google.charts.load('current', {
										'packages': ['corechart']
									});
									google.charts.setOnLoadCallback(drawChart);

									function drawChart() {
										var data = google.visualization.arrayToDataTable([
											['Year', 'Sales', 'Expenses'],
											['2004', 1000, 400],
											['2005', 1170, 460],
											['2006', 660, 1120],
											['2007', 1030, 540]
										]);

										var options = {
											title: 'Company Performance',
											curveType: 'function',
											legend: {
												position: 'bottom'
											}
										};

										var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

										chart.draw(data, options);
									}
								</script>
							</head>

							<body>
								<div id="curve_chart" style="width: 1000px; height: 1000px"></div>
							</body>


						</div>
					</div>
					<div class="row gtr-200">

						<div class="col-12">

							<!-- Features -->
							<section class="box features" style="display: none;">
								<h2 class="major"><span>最新消息</span></h2>
								<div class="">
									<div class="row guides">


									</div>
								</div>
							</section>

						</div>

					</div>

				</div>

			</section>


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
						<li>大同大學</li>
						<li>資訊經營<a href="http://localhost/testFix/index.php">WIOT</a></li>
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

</body>

</html>