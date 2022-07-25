//獲取資料
// db.collection('guides').get().then(snapshot =>{
//     setupGuides(snapshot.docs);
// });
// var uid = null;

//偵測登入狀況變化
auth.onAuthStateChanged((user) => {
    // console.log(user);
    if (user) {
        db.collection('guides').onSnapshot(snapshot => {
            setupGuides(snapshot.docs);
            setupUI(user);
        }, err => {
            console.log(err.message)
        });
        // uid = user.uid;
        db.collection('user')
        db.collection('user')
    } else {
        setupUI();
        setupGuides([]);
    }
});

//新增最新消息
$(function () {
    $(document.body).on('submit', '#createNews-form', function (e) {
        e.preventDefault();
        const createForm = document.querySelector('#createNews-form');
        // 獲得創建資料
        db.collection('guides').add({
            title: createForm['create-title'].value,
            content: createForm['create-content'].value,
        }).then(() => {
            //關閉對話框
            Swal.close();
            createForm.reset();

        }).catch(err => {
            console.log(err.message);
        })

    })
});

// 創建帳號
$(function () {
    $(document.body).on('submit', '#signup-form', function (e) {
        e.preventDefault();
        const signupForm = document.querySelector('#signup-form');
        // 獲得創建資料
        const email = signupForm['signup-email'].value;
        const password = signupForm['signup-password'].value;

        //創建使用者
        auth.createUserWithEmailAndPassword(email, password).then(cred => {
            return db.collection('users').doc(cred.user.uid).set({

                username: signupForm['signup-name'].value,
                phone: signupForm['signup-phone'].value,
                bio: signupForm['signup-bio'].value,
                uid: cred.user.uid,
            });

        }).then(() => {


            Swal.close();
            signupForm.reset();
        });

    });
});

//登出
const logout = document.querySelector('#logout');
logout.addEventListener('click', (e) => {
    e.preventDefault();
    auth.signOut();
});

// $(function () {
//     $(document.body).on('click', '#logout', function (e) {
//         e.preventDefault();
//         auth.signOut();
//     })
// });

//登入
$(function () {
    $(document.body).on('submit', '#login-form', function (e) {
        e.preventDefault();
        const loginForm = document.querySelector('#login-form');
        // 獲得使用者資料
        const email = loginForm['login-email'].value;
        const password = loginForm['login-password'].value;

        // 登入使用者帳號
        auth.signInWithEmailAndPassword(email, password).then(cred => {
            Swal.close();
            loginForm.reset();
        })

    })
});



