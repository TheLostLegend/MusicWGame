package com.serwylo.beatgame.network.data

import java.security.Timestamp


data class RegModel(
    var playerID:Int? = null ,
    var login:String?="" ,
    var email:String?="" ,
    var password:String?=""
//    ,
//    var registerdata: Timestamp ,
//    var lastlogindata:Timestamp
)