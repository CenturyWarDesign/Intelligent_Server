<?php
/**
 * Created by JetBrains PhpStorm.
 * User: wensonsmith
 * Date: 13-10-19
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */

require_once "jPush.class.php";

if(isset($_POST['content'])){
    $content = $_POST['content'];

    $result = jPush::pushMessage($content);

    echo "<pre>";
    print_r($result);
    exit;
}

?>

<html>
<head>

</head>

<body>

<button class="switch" value="10_1_1_1">LED_1_On</button><br/>
<button class="switch" value="10_1_0_1">LED_1_Off</button><br/>
<button class="switch" value="10_2_1_1">LED_2_On</button><br/>
<button class="switch" value="10_2_0_1">LED_2_Off</button><br/>
<button class="switch" value="10_3_1_1">LED_3_On</button><br/>
<button class="switch" value="10_3_0_1">LED_3_Off</button><br/>
<button class="switch" value="10_4_1_1">LED_4_On</button><br/>
<button class="switch" value="10_4_0_1">LED_4_Off</button><br/>
<button class="switch" value="10_5_1_1">LED_5_On</button><br/>
<button class="switch" value="10_5_0_1">LED_5_Off</button><br/>


<script src="jquery-1.10.2.min.js"></script>
<script>
    $(document).ready(function(){
        $(".switch").click(function(){
            var val = $(this).attr("value");
            $.ajax({
                url:"http://192.168.1.11/Intelligent/index.php",
                type:"POST",
                data:{"content":val},
                success:function(data){
                       alert(data);
                }
            })
        })
    })
</script>
</body>
</html>