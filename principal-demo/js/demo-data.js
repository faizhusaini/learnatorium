/* Standalone fictional demo data. No production data or APIs. */
window.DEMO_DATA = (() => {
  const raw = [
    ["LPS-1024","Haider Hussain","II","A",17,94,"Pending","Farah Hussain","98765 43210"],
    ["LPS-1001","Aarav Reddy","I","A",1,96,"Paid","Naveen Reddy","98490 11223"],
    ["LPS-1002","Ayesha Fatima","I","A",2,92,"Paid","Sana Fatima","97011 33445"],
    ["LPS-1003","Vihaan Sharma","I","A",3,88,"Pending","Rohit Sharma","99887 22334"],
    ["LPS-1004","Inaya Khan","I","B",4,97,"Paid","Adeel Khan","98481 77889"],
    ["LPS-1005","Reyansh Gupta","I","B",5,91,"Paid","Neha Gupta","99591 22446"],
    ["LPS-1006","Sara Ahmed","I","B",6,95,"Paid","Imran Ahmed","97002 54881"],
    ["LPS-1007","Arjun Rao","II","A",7,89,"Pending","Kiran Rao","98492 66880"],
    ["LPS-1008","Anaya Mehta","II","A",8,98,"Paid","Priyanka Mehta","90001 72637"],
    ["LPS-1009","Mohammed Rayyan","II","A",9,93,"Paid","Asif Hussain","97033 85214"],
    ["LPS-1010","Diya Nair","II","A",10,96,"Paid","Lakshmi Nair","98855 41001"],
    ["LPS-1011","Kabir Singh","II","A",11,87,"Pending","Amrita Singh","90105 61419"],
    ["LPS-1012","Myra Joshi","II","A",12,94,"Paid","Rakesh Joshi","98493 39002"],
    ["LPS-1013","Zain Ali","II","A",13,91,"Paid","Nida Ali","93920 70551"],
    ["LPS-1014","Ishita Verma","II","A",14,97,"Paid","Nitin Verma","98662 44108"],
    ["LPS-1015","Advik Kulkarni","II","A",15,90,"Pending","Sneha Kulkarni","90008 51240"],
    ["LPS-1016","Maryam Siddiqua","II","A",16,95,"Paid","Omer Siddiqui","99490 61777"],
    ["LPS-1017","Riya Kapoor","II","A",18,92,"Paid","Sonal Kapoor","97018 22880"],
    ["LPS-1018","Yusuf Mirza","II","A",19,89,"Paid","Adnan Mirza","98496 91921"],
    ["LPS-1019","Tara Menon","II","A",20,96,"Paid","Anil Menon","98850 72131"],
    ["LPS-1020","Dev Patel","II","A",21,94,"Paid","Mihir Patel","90000 86321"],
    ["LPS-1021","Alina Shaikh","II","A",22,93,"Paid","Saba Shaikh","90308 12944"],
    ["LPS-1022","Krish Malhotra","II","A",23,88,"Pending","Rhea Malhotra","99850 29292"],
    ["LPS-1023","Mahira Begum","II","A",24,97,"Paid","Nusrat Begum","98480 56341"],
    ["LPS-1025","Saanvi Iyer","II","A",25,95,"Paid","Meera Iyer","97040 81351"],
    ["LPS-1026","Rohan Das","II","A",26,90,"Paid","Suman Das","98491 43801"],
    ["LPS-1027","Hiba Rahman","II","A",27,96,"Paid","Faiza Rahman","99086 36741"],
    ["LPS-1028","Atharv Jain","II","A",28,91,"Paid","Pooja Jain","99518 22007"],
    ["LPS-1029","Meher Noor","II","A",29,94,"Paid","Aaliya Noor","97010 48212"],
    ["LPS-1030","Neil Thomas","II","A",30,92,"Paid","Susan Thomas","98488 11652"],
    ["LPS-1031","Zara Hussain","Nursery","A",5,97,"Paid","Farah Hussain","98765 43210"],
    ["LPS-1041","Rudra Bansal","II","A",31,93,"Paid","Amit Bansal","98490 73218"],
    ["LPS-1042","Afreen Zohra","II","A",32,96,"Paid","Shabana Zohra","97015 48261"],
    ["LPS-1043","Siddharth Naidu","II","A",33,90,"Paid","Madhavi Naidu","90002 71539"],
    ["LPS-1044","Sofia Parveen","II","A",34,95,"Paid","Nazia Parveen","99851 33074"],
    ["LPS-1032","Aditya Chandra","III","A",8,93,"Paid","Suresh Chandra","99481 20149"],
    ["LPS-1033","Samaira Bose","III","A",12,95,"Paid","Ananya Bose","97017 77011"],
    ["LPS-1034","Hamza Qureshi","III","B",14,86,"Pending","Sameer Qureshi","98483 44562"],
    ["LPS-1035","Nitya Reddy","IV","A",9,98,"Paid","Swathi Reddy","90005 91671"],
    ["LPS-1036","Arhaan Ansari","IV","A",18,96,"Paid","Sohail Ansari","99898 37746"],
    ["LPS-1037","Mira Krishnan","IV","B",7,92,"Paid","Vidya Krishnan","97039 55113"],
    ["LPS-1038","Eshan Roy","V","A",11,94,"Paid","Kunal Roy","98495 24791"],
    ["LPS-1039","Amina Syed","V","B",15,89,"Pending","Rabia Syed","99000 63742"],
    ["LPS-1040","Pranav Kumar","VII","B",21,74,"Pending","Vijay Kumar","98480 31948"]
  ];
  const students = raw.map((s, i) => ({id:`student-${i+1}`, admission:s[0], name:s[1], className:s[2], section:s[3], roll:s[4], attendance:s[5], feeStatus:s[6], guardian:s[7], contact:s[8], marks:{Mathematics:18,English:17,EVS:19,Hindi:16,Computer:20}}));
  return {
    students,
    teachers:[{id:"t1",name:"Ms. Priya",subjects:["English","EVS"],classes:["II-A","III-A","IV-B","V-A"]}],
    attendance:{date:"today",className:"II",section:"A",records:Object.fromEntries(students.filter(s=>s.className==="II"&&s.section==="A").map(s=>[s.id,"present"]))},
    fees:{"student-1":{annual:42000,paid:30000,pending:12000,nextInstallment:6000,due:"10 October 2026"},"student-31":{annual:36000,paid:30000,pending:6000,nextInstallment:6000,due:"10 October 2026"}},
    homework:[{id:"hw-default",className:"II",section:"A",subject:"Mathematics",title:"Fractions Practice",description:"Complete Exercise 4.2, Questions 1–8.",due:"Tomorrow",createdAt:"Today"}],
    announcements:[
      {id:"a1",title:"Gandhi Jayanti Holiday",audience:"Entire School",message:"School will remain closed on 2 October for Gandhi Jayanti.",time:"2 hours ago"},
      {id:"a2",title:"Annual Sports Day",audience:"Entire School",message:"Annual Sports Day registrations are now open.",time:"1 day ago"},
      {id:"a3",title:"Class V Field Trip",audience:"Class V",message:"Class V field trip consent forms are due Friday.",time:"2 days ago"}
    ],
    events:[{date:"28",month:"AUG",title:"Parent-Teacher Meeting",meta:"10:00 AM – 1:00 PM · School Campus"},{date:"02",month:"SEP",title:"Mid-Term Exam Begins",meta:"All Classes"}],
    timetable:{
      Monday:["English","Mathematics","EVS","Hindi","Computer","Art"], Tuesday:["Mathematics","English","Telugu","EVS","Physical Education","Hindi"],
      Wednesday:["EVS","English","Mathematics","Computer","Hindi","Art"], Thursday:["Hindi","Mathematics","English","EVS","Telugu","Physical Education"],
      Friday:["English","EVS","Mathematics","Computer","Art","Class Activity"]
    },
    exams:{name:"Periodic Test 1",marks:{Mathematics:18,English:17,EVS:19,Hindi:16,Computer:20}},
    children:[{id:"student-1",name:"Haider Hussain",classLabel:"II-A"},{id:"student-31",name:"Zara Hussain",classLabel:"Nursery-A"}]
  };
})();
