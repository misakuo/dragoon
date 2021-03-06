package com.github.ompc.robot.hexapod.dragoon.component.gait.builder;

import com.github.ompc.robot.hexapod.dragoon.component.gait.Gait;
import com.github.ompc.robot.hexapod.dragoon.component.gait.Pose;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collection;

import static com.github.ompc.robot.hexapod.dragoon.component.gait.builder.PoseOperation.addition;
import static com.github.ompc.robot.hexapod.dragoon.component.gait.builder.PoseOperation.merge;
import static java.util.Arrays.asList;

/**
 * 步态构造
 */
public class GaitBuilder {

    /*
     * 空步态
     */
    private static final Gait EMPTY_GAIT = new Gait(-1);

    /*
     * 初始步态
     */
    private final Gait main;

    private Gait currentGait;
    private Pose[] finallyPoses = new Pose[0];


    /**
     * 基于初始步态构造一个步态构造器
     *
     * @param gait
     */
    public GaitBuilder(Gait gait) {
        this.main = gait;
        this.currentGait = gait;
    }

    public GaitBuilder(long durationMs) {
        this(new Gait(durationMs));
    }

    /**
     * 步态构造器（初始给出一个执行时长为-1的无效步态）
     */
    public GaitBuilder() {
        this(EMPTY_GAIT);
    }


    public GaitBuilder newGait(long durationMs) {
        main.append(currentGait = new Gait(durationMs));
        return this;
    }

    private void computeCurrentGaitPose(PoseOperation operation, Collection<Pose> posesOfSrc, Pose[] posesOfDst) {
        finallyPoses = operation.operate(finallyPoses, posesOfDst);
        final Pose[] src = posesOfSrc.toArray(new Pose[posesOfSrc.size()]);
        posesOfSrc.clear();
        posesOfSrc.addAll(asList(operation.operate(src, posesOfDst)));
    }

    public GaitBuilder addition(Pose[] poses) {
        computeCurrentGaitPose(addition, currentGait.getPoses(), poses);
        return this;
    }

    public GaitBuilder merge(Pose[] poses) {
        computeCurrentGaitPose(merge, currentGait.getPoses(), poses);
        return this;
    }

    public Gait build() {
        return main;
    }


    public static void main(String... args) {

        final Gait gait = new GaitBuilder(1000)
                .merge(PoseOperations.stand(50))
                .newGait(500)
                .merge(PoseOperations.stand(50))
                .addition(PoseOperations.stand(50))
                .build();

        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(gait));

    }

}
