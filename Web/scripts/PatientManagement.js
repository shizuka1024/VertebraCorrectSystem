const UserList = document.querySelector('#user-list');
const key = sessionStorage.getItem('key');
const recentSearchList = document.querySelector('.recentSearch');

$(function () {
  $('.search-windows').click(async function () {
    Swal.fire({
      title: '搜尋病患',
      html: `
          <form id="searchUser-form">
                <div class="input-field">
                    輸入病患生分證字號:<br><br><input type="text" id="search-id" required /><br>
                </div>
                <br>
                <button>搜尋</button>
            </form>`,
      focusConfirm: false,
      showCancelButton: false,
      showConfirmButton: false,
    })
  })
})

$(function () {
  $(document.body).on('submit', '#searchUser-form', function (e) {
    e.preventDefault();
    const searchForm = document.querySelector('#searchUser-form');
    const searchID = searchForm['search-id'].value;
    db.collection('users').where("realId", "==", searchID).get()
      .then((querySnapshot) => {
        querySnapshot.forEach((doc) => {

          console.log(doc.id, " => ", doc.data());

          var searchResultForUserName = doc.data().username;
          var searchResultForRealId = doc.data().realId;
          var searchResultForEmail = doc.data().email;

          //搜尋歷史
          auth.onAuthStateChanged((user) => {
              db.collection("users").doc(user.uid).collection("recentSearch").add({
                name: searchResultForUserName,
                realId: searchResultForRealId,
                dateSent: firebase.firestore.FieldValue.serverTimestamp(),
              })
              console.log("recet")

          })

          Swal.close();
          searchForm.reset();
          Swal.fire({
            title: searchResultForUserName,
            html: `
                        <div>
                          <p>
                          ${searchResultForRealId}<br>
                          ${searchResultForEmail}<br>
                          </p>
                        </div>
                        `,
            focusConfirm: false,
            showCancelButton: false,
            showConfirmButton: false,
          })
        });
      }).catch(err => {
        console.log(err.message);
      })
  })
});



function renderUser(doc) {
  let li = document.createElement('li');
  let li2 = document.createElement('li2');
  let name = document.createElement('h5');
  let email = document.createElement('span');
  let realId = document.createElement('span');
  let phone = document.createElement('span');
  let address = document.createElement('span');

  li.setAttribute('data-id', doc.id);
  li2.setAttribute('data-id', doc.id);

  name.textContent = doc.data().username;

  email.innerHTML = '信箱:' + doc.data().email + '<br>';
  realId.innerHTML = 'ID :' + doc.data().realId + '<br>';
  phone.innerHTML = '電話:' + doc.data().phone + '<br>';
  address.innerHTML = '地址:' + doc.data().address + '<br>';


  li.appendChild(name);

  UserList.appendChild(li);
  UserList.appendChild(li2);

  name.addEventListener('click', (e) => {
    let id = e.target.parentElement.getAttribute('data-id');
    sessionStorage.setItem("key", id);

    li2.appendChild(email);
    li2.appendChild(realId);
    li2.appendChild(phone);
    li2.appendChild(address);

  })

}

db.collection('users').get().then((snapshot) => {
  snapshot.docs.forEach(doc => {
    renderUser(doc);
  })
})

const setupRecentSearch = (data) => {
  if (data.length) {
      let html = '';
      data.forEach(doc => {
          const recentSearch = doc.data();
          const li = `
          <li>
          <article class="box post-summary">
            <h4><a href="#">${recentSearch.name}</a></h4>
            <h5>${recentSearch.realId}</h5>
            <ul class="meta">
              <li class="icon fa-clock">${moment(recentSearch.dateSent.toDate()).startOf('sec').fromNow()}</li>
            </ul>
          </article>
        </li>
      `;
          html += li;
      });
      recentSearchList.innerHTML = html;
  } else {
    recentSearchList.innerHTML = '<br><h3 class="center-align">您尚未登入</h3>'
  }
}

auth.onAuthStateChanged((user) => {
  // console.log(user);
  if (user) {
      db.collection('users').doc(user.uid).collection('recentSearch').orderBy('dateSent', "desc").limit(3).onSnapshot(snapshot => {
          setupRecentSearch(snapshot.docs);
      }, err => {
          console.log(err.message)
      });
  } else {
      setupRecentSearchs([]);
  }
});