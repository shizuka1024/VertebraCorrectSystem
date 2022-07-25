const SearchBtn = document.getElementById('SearchButton');

google.charts.load('current', {
    'packages': ['corechart']
});
google.charts.setOnLoadCallback(drawChart);
async function drawChart() {
    // var idget = db.collection('users').doc('61rOU4lhV2NfsDe6I49noGKlLJk1');//.doc('34x9pcq3EMZryGOmMGcckqX8Uaa2');
    // var idG = await idget.get();
    // console.log(idG.id, '=>', idG.exists, '=>', idG.data().realId);
    
    var time = document.getElementById("time").value;
    var uid = document.getElementById("uid").value;

    if(uid == '' || time == ''){
        Swal.fire("請輸入病患id及時間!!");
        //return;
    }

    Time = time.substring(0,10)+" "+time.substring(11,16);
    console.log(Time);
    console.log(uid);

    //var lbsRef = db.collection('users').doc(uid.id).collection('statistics').doc(Time);
    var lbsRef = db.collection('users').doc('61rOU4lhV2NfsDe6I49noGKlLJk1').collection('statistics').doc(Time);
    var lbs = await lbsRef.get();
    
    var data = new google.visualization.DataTable();

    data.addColumn('string', '頸椎承受磅數');
    data.addColumn('number', '次數');
    var output = [];
    output.push(['12lbs', lbs.data().lbs12], ['27lbs', lbs.data().lbs27], ['40lbs', lbs.data().lbs40],
        ['49lbs', lbs.data().lbs49], ['60lbs', lbs.data().lbs60]);

    console.log(output);
    data.addRows(output);

    var options = {
        'legend': 'center',
        'width': 500,
        'height': 500
    };

    var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
    chart.draw(data, options);

    
}


//SearchBtn.addEventListener('click',alert);
SearchBtn.addEventListener('click', (e)=>{
    
        drawChart();
});

