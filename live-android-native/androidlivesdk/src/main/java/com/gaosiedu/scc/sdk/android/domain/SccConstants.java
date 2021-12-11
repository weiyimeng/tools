package com.gaosiedu.scc.sdk.android.domain;

/** @author */
public class SccConstants {
  /** 任务策略-日签到 */
  public static final String strategy_name_task_day_sign = "daySignTask";
  /** 任务策略-直播互动 */
  public static final String strategy_name_task_live_interact = "liveInteractTask";
  /** 任务策略-周签到 */
  public static final String strategy_name_task_week_sign = "weekSignTask";
  /** 任务策略-作业 */
  public static final String strategy_name_task_homeWork = "SubmitHomeWorkTask";
  /** 任务策略-直播 */
  public static final String strategy_name_task_live = "joinLiveTask";
  /** 任务策略-回放 */
  public static final String strategy_name_task_replay = "joinReplayTask";
  /** 额外策略-交作业教师打分 */
  public static final String strategy_name_normal_homeWork_evaluation = "normalHomeWorkEvaluation";
  /** 额外策略-学生提交作业 */
  public static final String strategy_name_normal_homeWork_submit = "normalHomeWorkSubmit";
  /** 消息策略-送花 */
  public static final String strategy_name_msg_flower = "flower";
  /** 消息策略-发送金币 */
  public static final String strategy_name_msg_send_gold = "sendGold";
  /** 消息策略-评价 */
  public static final String strategy_name_msg_assess = "comment";
  /** 消息策略-点赞 */
  public static final String strategy_name_msg_like = "like";
  /** 消息策略-回答问题 */
  public static final String strategy_name_msg_answer = "answer";
  /** 消息策略-直播登入 */
  public static final String strategy_name_msg_live_sign = "liveLoginSuccess";
  /** 消息策略-直播登入 */
  public static final String strategy_name_msg_live_out = "liveLoginOut";
  /** 消息策略-直播下课 */
  public static final String strategy_name_msg_live_overClass = "liveOverClass";
  /** 消息策略-直播上课 */
  public static final String strategy_name_msg_live_beginClass = "liveBeginClass";
  /** 消息策略-直播一段时间统计 */
  public static final String strategy_name_msg_live_time_count = "liveTimeCount";
  /** 消息策略-回放登入 */
  public static final String strategy_name_msg_replay_sign = "replayLoginSuccess";
  /** 消息策略-回放登出 */
  public static final String strategy_name_msg_replay_out = "replayLoginOut";
  /** 消息策略-回放一段时间统计 */
  public static final String strategy_name_msg_replay_time_count = "replayTimeCount";
  /** 消息策略-直播间状态更改 */
  public static final String strategy_name_msg_live_room_status = "changeLiveStatus";
  /** 消息策略-直播间公告 */
  public static final String strategy_name_msg_room_notice = "sendNotice";
  /** 等级绑定策略-金币绑定 */
  public static final String strategy_name_level_gold = "goldBind";
  /** 等级绑定策略-段位绑定 */
  public static final String strategy_name_level_level = "levelBind";
  /** 等级绑定策略-等级绑定 */
  public static final String strategy_name_level_rank = "rankBind";
  /** 学员 */
  public static final String user_role = "student";
  /** 教师 */
  public static final String teacher_role = "teacher";
  /** 助教 */
  public static final String assistant_role = "assistant";
  /** 学员登入 */
  public static final String loginSuccess = "loginSuccess";
  /** 学员登出 */
  public static final String loginOut = "loginOut";

  /** 教师发送金币 */
  public static final String teacherSend = "teacherSend";

  /** 教师批改作业 优 1 */
  public static final String normalSubmit_best = "1";

  /** 教师批改作业 良 1 */
  public static final String normalSubmit_good = "2";

  /** 教师批改作业 中 1 */
  public static final String normalSubmit_general = "3";

  /** 教师批改作业 差 1 */
  public static final String normalSubmit_bad = "4";

  /** 一级目录 直播行为 */
  public static final String live_root_type = "live";
  /** 一级目录 回放行为 */
  public static final String replay_root_type = "replay";
  /** 一级目录 任务行为 */
  public static final String task_root_type = "task";
  /** 一级目录 平常行为 */
  public static final String normal_root_type = "normal";

  /** 作业状态 unsubmitted未提交，correcting批改中，corrected已批改 */
  public static final String user_exercise_status_unsubmitted = "unsubmitted";
  public static final String user_exercise_status_correcting = "correcting";
  public static final String user_exercise_status_corrected = "corrected";


  /** 订正错题 */
  public static final Short user_wrong_question_correct = 1;
  /** 未订正错题 */
  public static final Short user_wrong_question_uncorrect = 0;
  /** 收藏错题 */
  public static final Short user_wrong_question_collect = 1;
  /** 取消收藏错题 */
  public static final Short user_wrong_question_cancel_collect = 0;
  /** 删除状态。0删除*/
  public static final Short user_wrong_question_del_status = 0;

  private SccConstants() {}
}
