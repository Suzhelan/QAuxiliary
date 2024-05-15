/*
 * QAuxiliary - An Xposed module for QQ/TIM
 * Copyright (C) 2019-2024 QAuxiliary developers
 * https://github.com/cinit/QAuxiliary
 *
 * This software is an opensource software: you can redistribute it
 * and/or modify it under the terms of the General Public License
 * as published by the Free Software Foundation; either
 * version 3 of the License, or any later version as published
 * by QAuxiliary contributors.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the General Public License for more details.
 *
 * You should have received a copy of the General Public License
 * along with this software.
 * If not, see
 * <https://github.com/cinit/QAuxiliary/blob/master/LICENSE.md>.
 */

package me.hd.hook

import cc.ioctl.util.hookBeforeIfEnabled
import io.github.qauxv.base.annotation.FunctionHookEntry
import io.github.qauxv.base.annotation.UiItemAgentEntry
import io.github.qauxv.dsl.FunctionEntryRouter
import io.github.qauxv.hook.CommonSwitchFunctionHook
import io.github.qauxv.util.Initiator
import io.github.qauxv.util.QQVersion
import io.github.qauxv.util.requireMinQQVersion

@FunctionHookEntry
@UiItemAgentEntry
object FakeVoiceTime : CommonSwitchFunctionHook() {

    override val name = "伪装语音时间"
    override val description = "伪装发送语音文件时长[1s~300s]"
    override val uiItemLocation = FunctionEntryRouter.Locations.Entertainment.ENTERTAIN_CATEGORY
    override val isAvailable = requireMinQQVersion(QQVersion.QQ_8_9_88)

    override fun initOnce(): Boolean {
        val recorderApiClass = Initiator.loadClass("com.tencent.mobileqq.ptt.temp.api.impl.QQRecorderTempApiImpl")
        val messageRecordClass = Initiator.loadClass("com.tencent.mobileqq.data.MessageRecord")
        val getFilePlayTimeMethod = recorderApiClass.getDeclaredMethod("getFilePlayTime", messageRecordClass)
        hookBeforeIfEnabled(getFilePlayTimeMethod) { param ->
            param.result = 1
        }
        return true
    }
}