const guideList = document.querySelector('.guides');
const loggedOutLinks = document.querySelectorAll('.logged-out');
const loggedInLinks = document.querySelectorAll('.logged-in');
const accountDetails = document.querySelector('.account-details')
var userEmail = null;


const setupUI = (user) => {
    if (user) {
        //
        // const html = `
        //     <div>Logged in as ${user.email}</div>
        // `;

        userEmail = user.email;



        //
        loggedInLinks.forEach(item => item.style.display = 'block')
        loggedOutLinks.forEach(item => item.style.display = 'none');
    } else {
        //

        //
        loggedInLinks.forEach(item => item.style.display = 'none');
        loggedOutLinks.forEach(item => item.style.display = 'block');
    }
}

/* 設定資料 */
const setupGuides = (data) => {

    if (data.length) {
        let html = '';
        data.forEach(doc => {
            const guide = doc.data();
            const li = `



                <div class="col-3 col-6-medium col-12-small ">

                    <!-- Feature -->
                    <section class="box feature">
                    <a href="#" class="image featured"><img src="images/Arm-IOT2.jpeg" alt="" /></a>
                        <div><h4>${guide.title}</h4></div>
                        <div><p>${guide.content}</p></div>
                     </section>

                </div>




        `;
            html += li;
        });

        guideList.innerHTML = html;
    } else {
        guideList.innerHTML = '<br><h3 class="center-align">您尚未登入</h3>'
    }

}

/* 登入 */
$(function () {
    $('.login-windows').click(async function () {
        const {
            value: formValues
        } = await Swal.fire({
            title: '登入',
            html: `
            <form id="login-form">
                <div class="input-field">
                    電子信箱:<br><input type="email" id="login-email" required /><br>
                </div>
                <div class="input-field">
                    密碼:<br><input type="password" id="login-password" required /><br>
                </div>
                <br>
                <button>登入</button>
            </form>`,
            focusConfirm: false,
            showCancelButton: false,
            showConfirmButton: false,





        })


    })
})

/* 創建帳號 */
$(function () {
    $('.signup-windows').click(async function () {
        Swal.fire({
            title: '創建帳號',
            html: `
            <form id="signup-form">
                <div class="input-field">
                    電子信箱:<br><input type="email" id="signup-email" required /><br>
                </div>
                <div class="input-field">
                    密碼:<br><input type="password" id="signup-password" required /><br>
                </div>
                <div class="input-field">
                    姓名:<br><input type="text" id="signup-name" required /><br>
                </div>
                <div class="input-field">
                    個人簡歷:<br><input type="text" id="signup-bio" required /><br>
                </div>
                <div class="input-field">
                    電話:<br><input type="text" id="signup-phone" required /><br>
                </div>
                <div class="input-field">
                    所屬醫院:<br>
                    <select id="signup-hospital">
                        <option>*  請選擇您所屬機構  *</option>
                        <option>大同大學</option>
                        <option>大同公司</option>
                        <option>新光醫院</option>
                        <option>台大醫院</option>
                        <option>榮民總醫院</option>
                        <option>個人使用者</option>
                    </select>
                </div><br>
                <button>創建</button>
            </form>`,
            focusConfirm: false,
            showCancelButton: false,
            showConfirmButton: false



        })

    })
})

/* 創建消息 */
$(function () {
    $('.createNews-windows').click(async function () {
        const {
            value: formValues
        } = await Swal.fire({
            title: '新增最新消息',
            html: `
            <form id="createNews-form">
                <div class="input-field">
                    標題:<br><input type="title" id="create-title" required /><br>
                </div>
                <div class="input-field">
                    內文:<br><textarea name="content"  rows="6"  cols="40" id="create-content"required></textarea><br>
                </div>
                <br>
                <button>新增</button>
            </form>`,
            focusConfirm: false,
            showCancelButton: false,
            showConfirmButton: false,
        })
    })
})

/* 帳號資料 */
$(function () {
    $('.accountDetails-windows').click(async function () {
        const {
            value: formValues
        } = await Swal.fire({
            title: '帳號資料',
            html: `
            <div><p><strong>登入信箱 : </strong>${userEmail}</p></div>
            `,

            focusConfirm: false,
            showCancelButton: false,
            showConfirmButton: false,
        })
    })
})





