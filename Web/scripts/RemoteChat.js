const ChatList = document.querySelector('#chat-list');
const key = sessionStorage.getItem('key');

db.collection('users').where("usergroup", "==", "patient").get().then((snapshot) => {
    snapshot.docs.forEach(doc => {
        renderUser(doc);
    })
})

function renderUser(doc) {
    let li = document.createElement('li');
    let name = document.createElement('h5');

    auth.onAuthStateChanged((user) => {
        if (user) {
            li.setAttribute('chat-room-id', doc.id + user.uid);
            console.log(doc.id + user.uid);
        } else {
            console.log("User not logged in or has just logged out.");
        }
    })

    li.setAttribute('class', "chat-windows");
    name.textContent = doc.data().username;

    li.appendChild(name);


    ChatList.appendChild(li);

    li.addEventListener('click', (e) => {
        let id = e.target.parentElement.getAttribute('chat-room-id');
        sessionStorage.setItem("key", id);
    })
}

$(function () {
    $(document.body).on('click', '.chat-windows', async function (e) {
        e.preventDefault();
        const chatroomid = sessionStorage.getItem('key');

        db.collection("messages").doc(chatroomid).collection("message").orderBy("dateSent").onSnapshot((snapshot) => {
            snapshot.docChanges().forEach((change) => {
                if (change.type === "added") {
                    const msg = "<li>" + change.doc.data().name + " : " + change.doc.data().message + "</li>";
                    document.getElementById("messages").innerHTML += msg;
                }
            });
        });
        const {
            value: formValues
        } = await Swal.fire({
            title: 'name',
            html: `
            <form id="chat-form">
                <div class="input-field" style="overflow: scroll; width: 400px; height: 600px; ">
                    <ul id="messages"></ul>
                </div>
                <div class="input-field">
                    <input type="text" id="chat-record" required /><br>
                </div>
                <br>
                <button id="send-message">傳送</button>
            </form>`,
            focusConfirm: false,
            showCancelButton: false,
            showConfirmButton: false,
        })
    })
});

$(function () {
    $(document.body).on('submit', '#chat-form', function (e) {
        e.preventDefault();
        const chatForm = document.querySelector('#chat-form');
        const message = chatForm['chat-record'].value;
        const chatroomid = sessionStorage.getItem('key');
        return auth.onAuthStateChanged(user => {
            if (message == '') {
                console.log('e');
                return;
            } else {
                chatForm['chat-record'].value = "";
                db.collection('users').doc(user.uid).get().then((doc) => {
                    db.collection("messages").doc(chatroomid).collection("message").add({
                        name: doc.data().username,
                        message: message,
                        uid: user.uid,
                        dateSent: firebase.firestore.FieldValue.serverTimestamp(),
                    }).then((docRef) => {
                        console.log("Document written with ID: ", docRef.id);
                    }).catch((error) => {
                        console.error("Error adding document: ", error);
                    })
                })
            }
        })
    })
});