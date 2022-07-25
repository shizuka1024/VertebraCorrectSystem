const btn = document.getElementById('storedata');
const FeedbackList = document.querySelector('#feedback-list');
var ref = db.collection('users');
const key = sessionStorage.getItem('key');

function renderFeedBack(doc) {
    let li = document.createElement('li');
    let li2 = document.createElement('li2');
    let Suggest = document.createElement('span');
    let saySomething = document.createElement('span');
    let usetime = document.createElement('span');

    li.setAttribute('data-id', doc.id);
    li2.setAttribute('data-id', doc.id);

    usetime.innerHTML = '<b>使用時間 : ' + doc.data().usetime + '</b><br>';
    saySomething.innerHTML = '想說的話 : ' + doc.data().saySomething + '<br>';
    Suggest.innerHTML = '您的建議 : ' + doc.data().Suggest + '<br>';

    li.appendChild(usetime);
    FeedbackList.appendChild(li);
    FeedbackList.appendChild(li2);


    li2.appendChild(saySomething);
    li2.appendChild(Suggest);

}
auth.onAuthStateChanged(user => {
    if (user) {
        db.collection('FeedBack').where("uid", "==", user.uid).get().then((snapshot) => {
            snapshot.docs.forEach(doc => {
                renderFeedBack(doc);
            })
        })
    }
});

// ref.get().then(querySnapshot => {
//     querySnapshot.forEach(doc => {
//         auth.onAuthStateChanged(user => {
//             if (user) {
//                 console.log(doc.id, doc.data());
//                 const name = document.querySelector('#user');
//                 name.textContent = doc.data(uid).username;
//             }
//         });


//     });
// });



btn.addEventListener('click', (e) => {
    e.preventDefault();
    var usetime = document.getElementById("usetime").value;
    var say = document.getElementById("say").value;
    var improve = document.getElementById("improve").value;
    if (say == '' || improve == '') {

        return;
    }
    auth.onAuthStateChanged(user => {
        if (user) {
            db.collection("FeedBack").add({
                usetime: usetime,
                saySomething: say,
                Suggest: improve,
                timestamp: firebase.firestore.FieldValue.serverTimestamp(),
                uid: user.uid,

            })
                .then(() => {
                    Swal.fire({
                        title: '感謝您的意見',
                        text: "我們將會變得更好",
                        confirmButtonColor: '#ADD5A8',
                        confirmButtonText: 'OK'
                    });

                    console.log("Document successfully written!");
                    document.getElementById("feedbackForm").reset();
                })
                .catch((error) => {
                    console.error("Error writing document: ", error);
                });
            console.log(e);
        }
    });

}, false);

