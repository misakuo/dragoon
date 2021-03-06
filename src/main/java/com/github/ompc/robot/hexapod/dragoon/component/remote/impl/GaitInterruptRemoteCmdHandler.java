package com.github.ompc.robot.hexapod.dragoon.component.remote.impl;

import com.github.ompc.robot.hexapod.dragoon.component.gait.GaitCtlCom;
import com.github.ompc.robot.hexapod.dragoon.component.remote.RemoteCmd;
import com.github.ompc.robot.hexapod.dragoon.component.remote.RemoteCmdHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.github.ompc.robot.hexapod.dragoon.component.remote.RemoteCmd.Type.GAIT_INTERRUPT;

@Component
public class GaitInterruptRemoteCmdHandler implements RemoteCmdHandler<Void> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GaitCtlCom gaitCtlCom;

    @Override
    public RemoteCmd.Type getRemoteCmdType() {
        return GAIT_INTERRUPT;
    }

    @Override
    public void onCmd(RemoteCmd<Void> remoteCmd) {
        gaitCtlCom.interrupt();
        logger.info("gait is interrupt!");
    }

}
