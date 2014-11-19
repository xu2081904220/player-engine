player-engine
=============

负责处理多媒体的播放行为

PlayerEngine接口不对外开放，防止出现错误的调用，外部想进行播放，暂停，上下切换等操作时，需要通过调用ActionHandler中的execute接口，
并传入对应的枚举类型来执行。

注：MediaBase是准备作为一个jar包存在的，故其中不会有任何android 4大组件相关的类。
