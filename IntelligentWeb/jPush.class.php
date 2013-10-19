<?php
/**
 * Created by JetBrains PhpStorm.
 * User: wensonsmith
 * Date: 13-10-19
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */

class jPush {

    static private $apiUrl = "http://api.jpush.cn:8800/v2/push";
    static private $num = 11232;
    static private $appKey = "550943fb320cf916b5a78c41";
    //极光推送portal 上分配的 appKey 的验证串(masterSecret)
    static private $masterSecret = "68c01e4660be175e19844c47";
    //4、广播：对 app_key 下的所有用户推送消息。
    static private $receiver_type = 4;

    static public function pushMessage($text){

        $content = array(
            "message"=>$text,
            "n_content"=>$text,
            "n_extras"=>''
        );

        $msg = array(
            "sendno"=> self::$num,
            "app_key"=>self::$appKey,
            "receiver_type"=>self::$receiver_type,
            "verification_code"=>md5(self::$num.self::$receiver_type.self::$masterSecret),
            "msg_type"=>2, //1.通知  2.自定义消息（Android Only）
            "msg_content"=>json_encode($content,JSON_HEX_APOS),
            "platform"=>"android,ios"
        );

        $result = self::curlRequest(self::$apiUrl,$msg,"POST");

        return $result;
    }

    static private function curlRequest($url,$params,$type){
        $ch = curl_init();
        curl_setopt($ch,CURLOPT_URL,$url);
        curl_setopt($ch,CURLOPT_HTTPHEADER,array('Content-type: application/x-www-form-urlencoded;charset=UTF-8'));
        curl_setopt($ch,CURLOPT_TIMEOUT,10);
        curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);

        if($type == "POST")
        {
            curl_setopt($ch,CURLOPT_POST,true);
            curl_setopt($ch,CURLOPT_POSTFIELDS,http_build_query($params));
        }

        $result = curl_exec($ch);
        curl_close($ch);

        return json_decode($result,true);
    }

}