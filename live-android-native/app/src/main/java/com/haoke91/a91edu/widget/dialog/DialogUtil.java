package com.haoke91.a91edu.widget.dialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.daquexian.flexiblerichtextview.FlexibleRichTextView;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.BasePagerAdapter;
import com.haoke91.a91edu.adapter.LiveRoomAdapter;
import com.haoke91.a91edu.entities.player.Answer;
import com.haoke91.a91edu.entities.player.ListStudent;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.flowlayout.FlowLayout;
import com.haoke91.a91edu.widget.flowlayout.TagAdapter;
import com.haoke91.a91edu.widget.flowlayout.TagFlowLayout;
import com.haoke91.a91edu.widget.ratingBar.BaseRatingBar;
import com.haoke91.a91edu.widget.ratingBar.ScaleRatingBar;
import com.haoke91.baselibrary.model.VideoUrl;
import com.haoke91.baselibrary.utils.ACallBack;
import com.haoke91.baselibrary.utils.DensityUtil;
import com.haoke91.baselibrary.utils.ICallBack;
import com.haoke91.baselibrary.views.DragViewGroup;
import com.haoke91.baselibrary.views.QuestionLayout;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;

import org.scilab.forge.jlatexmath.core.AjLatexMath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/7/17 17:04
 */
public class DialogUtil {
    private static DialogUtil instance = null;
    public static int KEYID = 0;
    
    private DialogUtil() {
        AjLatexMath.init(Utils.getApp());
    }
    
    public static DialogUtil getInstance() {
        if (instance == null) {
            synchronized (DialogUtil.class) {
                if (instance == null) {
                    instance = new DialogUtil();
                }
            }
        }
        return instance;
    }
    
    /**
     * 显示问答弹框，
     *
     * @param parent
     * @param time   秒
     */
    public void choice(final ViewGroup parent, ArrayList<Answer> choices, final long time, final ACallBack<String> callBack) {
        if (parent.getChildCount() > 0) {
            parent.removeAllViews();
        }
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(R.layout.dialog_xuanze, parent);
        ViewGroup.LayoutParams layoutParams = view.getChildAt(0).getLayoutParams();
        layoutParams.width = parent.getResources().getDisplayMetrics().widthPixels * 2 / 3;
        view.getChildAt(0).setLayoutParams(layoutParams);
        final QuestionLayout fl_question = view.findViewById(R.id.fl_question);
        final RadioGroup rg_answer = view.findViewById(R.id.rg_answer);
        final TextView tv_count_time = view.findViewById(R.id.tv_count_time);
        fl_question.startTimeCount(time, new ACallBack<Long>() {
            @Override
            public void call(Long aLong) {
                tv_count_time.setText(aLong + "s");
                if (aLong == 0) {
                    fl_question.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearParent(parent);
                            
                        }
                    }, 1000);
                }
            }
        });
        parent.setVisibility(View.VISIBLE);
        view.findViewById(R.id.tv_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = rg_answer.findViewById(rg_answer.getCheckedRadioButtonId());
                if (rb == null) {
                    ToastUtils.showShort("请选择至少一个选项");
                    return;
                }
                callBack.call((String) rb.getTag());
                clearParent(parent);
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearParent(parent);
            }
        });
        for (Answer answer : choices) {
            RadioButton rb = newChoiceRadioButton(parent.getContext(), answer.getChoose());
            rb.setTag(answer.getChoose());
            rb.setId(View.generateViewId());
            rg_answer.addView(rb);
        }
        rg_answer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            
            }
        });
        
        
        view.findViewById(R.id.tv_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View checkedBtn = rg_answer.findViewById(rg_answer.getCheckedRadioButtonId());
                if (checkedBtn == null) {
                    ToastUtils.showShort("请选择至少一个选项");
                    return;
                }
                callBack.call((String) checkedBtn.getTag());
                clearParent(parent);
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearParent(parent);
            }
        });
    }
    
    private SpannableString getColorSpan(String res, String des, int color) {
        SpannableString span = new SpannableString(res);
        span.setSpan(new ForegroundColorSpan(color), res.indexOf(des), res.indexOf(des) + des.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
    
    /**
     * 判断题
     *
     * @param parent
     * @param time   秒
     */
    public void showJudge(final ViewGroup parent, String left, String right, long time, final ACallBack callBack) {
        if (parent.getChildCount() > 0) {
            parent.removeAllViews();
        }
        View view = LayoutInflater.from(Utils.getApp()).inflate(R.layout.dialog_judge, parent);
        final QuestionLayout fl_question = view.findViewById(R.id.fl_question);
        final TextView tv_count_time = view.findViewById(R.id.tv_count_time);
        fl_question.startTimeCount(30, new ACallBack<Long>() {
            @Override
            public void call(Long aLong) {
                tv_count_time.setText(aLong + "s");
                
                if (aLong == 0) {
                    fl_question.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearParent(parent);
                            
                        }
                    }, 1000);
                }
            }
        });
        parent.setVisibility(View.VISIBLE);
        view.findViewById(R.id.tv_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearParent(parent);
            }
        });
        
    }
    
    /**
     * 填空题
     *
     * @param parent
     */
    public void fillBlank(final ViewGroup parent, final ACallBack callBack) {
        if (parent.getChildCount() > 0) {
            parent.removeAllViews();
        }
        final View view = LayoutInflater.from(Utils.getApp()).inflate(R.layout.dialog_fillblank, parent);
        final QuestionLayout fl_question = view.findViewById(R.id.fl_question);
        final TextView tv_count_time = view.findViewById(R.id.tv_count_time);
        fl_question.startTimeCount(30, new ACallBack<Long>() {
            @Override
            public void call(Long aLong) {
                tv_count_time.setText(aLong + "s");
                
                if (aLong == 0) {
                    fl_question.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearParent(parent);
                            
                        }
                    }, 1000);
                }
            }
        });
        parent.setVisibility(View.VISIBLE);
        view.findViewById(R.id.tv_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearParent(parent);
                
            }
        });
        final EditText et_input = view.findViewById(R.id.et_answer);
        final View btn_commit = view.findViewById(R.id.tv_commit);
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearParent(parent);
            }
        });
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    btn_commit.setSelected(true);//hide the dialog when commited
                }
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            
            }
        });
    }
    
    /**
     * 看题并填空题
     *
     * @param parent
     */
    public void lookAndfillBlank(final ViewGroup parent, String body, long time, final ACallBack<String> callBack) {
        if (parent.getChildCount() > 0) {
            parent.removeAllViews();
        }
        View view = LayoutInflater.from(Utils.getApp()).inflate(R.layout.dialog_lookandfillblank, parent);
        final QuestionLayout fl_question = view.findViewById(R.id.fl_question);
        final TextView tv_count_time = view.findViewById(R.id.tv_count_time);
        FlexibleRichTextView tv_body = view.findViewById(R.id.tv_body);
        final EditText et_anser = view.findViewById(R.id.et_answer);
        tv_body.setText(body);
        //        ViewGroup.LayoutParams lp = fl_question.getLayoutParams();
        //        lp.width = parent.getResources().getDisplayMetrics().widthPixels * 2 / 3;
        //        fl_question.setLayoutParams(lp);
        fl_question.startTimeCount(time, new ACallBack<Long>() {
            @Override
            public void call(Long aLong) {
                tv_count_time.setText(aLong + "s");
                
                if (aLong == 0) {
                    fl_question.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearParent(parent);
                            
                        }
                    }, 1000);
                }
            }
        });
        parent.setVisibility(View.VISIBLE);
        view.findViewById(R.id.tv_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_anser.getText().toString() == null || et_anser.getText().toString().length() < 1) {
                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                    ToastUtils.showShort("答案不能为空");
                    return;
                }
                callBack.call(et_anser.getText().toString());
                clearParent(parent);
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearParent(parent);
            }
        });
    }
    
    /**
     * 选择题
     */
    public void lookAndChoice(final ViewGroup parent, long time, String body, final ArrayList<Answer> answers, final ACallBack<String> aCallBack) {
        if (parent.getChildCount() > 0) {
            parent.removeAllViews();
        }
        View view = LayoutInflater.from(Utils.getApp()).inflate(R.layout.dialog_lookandchoice, parent);
        final QuestionLayout fl_question = view.findViewById(R.id.fl_question);
        final TextView tv_count_time = view.findViewById(R.id.tv_count_time);
        fl_question.startTimeCount(time, new ACallBack<Long>() {
            @Override
            public void call(Long aLong) {
                tv_count_time.setText(aLong + "s");
                
                if (aLong == 0) {
                    fl_question.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearParent(parent);
                            
                        }
                    }, 1000);
                }
            }
        });
        parent.setVisibility(View.VISIBLE);
        final RadioGroup rg_answer = view.findViewById(R.id.rg_answer);
        if (!ObjectUtils.isEmpty(answers)) {
            for (Answer answer : answers) {
                RadioButton rb = newRadioButton(parent.getContext(), answer.getChoose(), answer.getChooseContent());
                rb.setTag(answer.getChoose());
                rb.setId(View.generateViewId());
                rg_answer.addView(rb);
            }
        }
        rg_answer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            }
        });
        view.findViewById(R.id.tv_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View checkedBtn = rg_answer.findViewById(rg_answer.getCheckedRadioButtonId());
                if (checkedBtn == null) {
                    ToastUtils.showShort("请选择至少一个选项");
                    return;
                }
                aCallBack.call((String) checkedBtn.getTag());
                clearParent(parent);
                //                ACallBack.call();
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearParent(parent);
            }
        });
        /**
         * 题干
         */
        FlexibleRichTextView tv_question = view.findViewById(R.id.tv_question);
        tv_question.setText(body);
        
        
    }
    
    /**
     * 计时器
     *
     * @param parent
     * @param seconds
     */
    public void showTimer(final ViewGroup parent, long seconds, final ACallBack<String> callBack) {
        if (parent.getChildCount() > 0) {
            parent.removeAllViews();
        }
        if (parent instanceof DragViewGroup) {
            ((DragViewGroup) parent).enableDrag(false);
        }
        View view = LayoutInflater.from(Utils.getApp()).inflate(R.layout.dialog_showtimer, parent);
        final QuestionLayout fl_question = view.findViewById(R.id.fl_question);
        final TextView tv_count_time = view.findViewById(R.id.tv_count_time);
        LottieAnimationView lottie = view.findViewById(R.id.lottieAnimationView);
        lottie.playAnimation();
        fl_question.startTimeCount(seconds, new ACallBack<Long>() {
            @Override
            public void call(Long aLong) {
                StringBuffer result = new StringBuffer();
                long tenDigit = aLong / 60;
                long unitDigit = aLong % 60;
                if (aLong / 60 > 0) {
                    if (tenDigit >= 10) {
                        result.append(tenDigit);
                    } else {
                        result.append("0" + tenDigit);
                    }
                    result.append(":");
                    if (unitDigit >= 10) {
                        result.append(unitDigit);
                    } else {
                        result.append("0" + unitDigit);
                    }
                } else {
                    if (unitDigit >= 10) {
                        result.append("00:" + unitDigit);
                    } else {
                        result.append("00:0" + unitDigit);
                    }
                }
                tv_count_time.setText(result);
                
                if (aLong == 0) {
                    fl_question.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (parent instanceof DragViewGroup) {
                                ((DragViewGroup) parent).enableDrag(true);
                            }
                            clearParent(parent);
                            if (callBack != null) {
                                callBack.call("");
                            }
                        }
                    }, 1000);
                }
            }
        });
        parent.setVisibility(View.VISIBLE);
    }
    
    /**
     * 课后评价
     *
     * @param parent
     */
    List<String> strings;
    
    public void commitEvaluate(final ViewGroup parent, String gold, String progress, final ACallBack<String[]> aCallBack) {
        if (parent.getChildCount() > 0) {
            parent.removeAllViews();
        }
        final String[] evaluates = new String[4];
        //        String[][] tagss=new String[5][3];
        //        tagss[0]=new String[]{"重点模糊","内容浅易","感受平平"};
        //        tagss[1]=new String[]{"内容充实","流畅完整","兴趣不足"};
        //        tagss[2]=new String[]{"思路清晰","注重落实","认真负责"};
        //        tagss[3]=new String[]{"重点精准","专业过硬","激发兴趣"};
        //        tagss[5]=new String[]{"内容精彩","干货满满","激情四射"};
        final int[] comments = {R.array.start_zero, R.array.start_one, R.array.start_two, R.array.start_three, R.array.start_four, R.array.start_five};
        View view = LayoutInflater.from(Utils.getApp()).inflate(R.layout.dialog_evaluatecommit, parent);
        //        final EditText et_evaluate = view.findViewById(R.id.et_evaluate);
        final TagFlowLayout tf_content = view.findViewById(R.id.tf_content);
        strings = Arrays.asList(Utils.getApp().getResources().getStringArray(R.array.start_zero));
        TextView tvGoldObtain = view.findViewById(R.id.tv_gold_obtain);
        TextView tvProgressObtain = view.findViewById(R.id.tv_progressObtain);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(String.format("Hi %s，下课喽", UserManager.getInstance().getLoginUser().getName()));
        tvGoldObtain.setText(gold);
        tvProgressObtain.setText(progress);
        final TagAdapter<String> tagAdapter = new TagAdapter<String>(strings) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(Utils.getApp()).inflate(R.layout.item_evaluate_select, parent, false);
                tv.setText(s);
                return tv;
            }
            
            @Override
            public void onSelected(int position, View view) {
                view.setSelected(true);
                ((TextView) view).setTextColor(Color.parseColor("#75c82b"));
                //                et_evaluate.append(strings.get(position) + ",");
            }
            
            @Override
            public void unSelected(int position, View view) {
                ((TextView) view).setTextColor(Color.parseColor("#000000"));
                
                view.setSelected(false);
                
            }
        };
        tf_content.setAdapter(tagAdapter);
        
        tf_content.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //                et_evaluate.append(strings.get(position) + ",");
                Log.e("tag", "22232===" + strings.get(position));
                evaluates[1] = strings.get(position);
                return false;
            }
        });
        ScaleRatingBar scaleRatingBar = view.findViewById(R.id.scaleRatingBar);
        scaleRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                Log.e("tag", "rating====" + rating);
                int comment = comments[(int) rating];
                evaluates[0] = (int) rating + "";
                strings = Arrays.asList(Utils.getApp().getResources().getStringArray(comment));
                tagAdapter.setDate(strings);
                tagAdapter.notifyDataChanged();
            }
        });
        RadioGroup rg = view.findViewById(R.id.rg_comment);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                CharSequence text = ((RadioButton) group.findViewById(checkedId)).getText();
                evaluates[2] = text.toString();
            }
        });
        final EditText et_input = view.findViewById(R.id.et_evaluate);
        view.findViewById(R.id.tv_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = et_input.getText().toString().trim();
                evaluates[3] = input;
                aCallBack.call(evaluates);
                clearParent(parent);
            }
        });
        view.findViewById(R.id.iv_closeDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearParent(parent);
            }
        });
        parent.setVisibility(View.VISIBLE);
    }
    
    /**
     * 获得金币动画
     *
     * @param context
     */
    public void showGoldAnim(Context context, int gold) {
        final FrameLayout content = ((Activity) context).findViewById(android.R.id.content);
        final View view = LayoutInflater.from(context).inflate(R.layout.layout_gold_anim, null);
        content.addView(view);
        ((Activity) context).onAttachedToWindow();
        LottieAnimationView view_gold = view.findViewById(R.id.lottieAnimationView_gold);
        final TextView tvGoldValue = view.findViewById(R.id.tvGoldValue);
        tvGoldValue.setText("金币 +" + gold);
        view_gold.setAnimation("gold_anim.json");
        view_gold.playAnimation();
        //
        //        content.postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                content.removeView(view);
        //            }
        //        }, 3000);
        //金币移动动画
        TextView originGold = content.findViewById(R.id.tv_goldNum);
        final int[] originLocation = new int[2];
        final AnimatorSet anims = new AnimatorSet();
        if (originGold != null) {
            originGold.getLocationOnScreen(originLocation);
        }
        final int[] currentLocation = new int[2];
        tvGoldValue.post(new Runnable() {
            @Override
            public void run() {
                tvGoldValue.getLocationOnScreen(currentLocation);
                //去除放大缩小动画
                //                ObjectAnimator smallToBigX = ObjectAnimator.ofFloat(tvGoldValue, "scaleX", 0.5f, 2f).setDuration(300);
                //                ObjectAnimator smallToBigY = ObjectAnimator.ofFloat(tvGoldValue, "scaleY", 0.5f, 2f).setDuration(300);
                //                ObjectAnimator bigToSmallX = ObjectAnimator.ofFloat(tvGoldValue, "scaleX", 2f, 0.5f).setDuration(800);
                //                ObjectAnimator bigToSmallY = ObjectAnimator.ofFloat(tvGoldValue, "scaleY", 2f, 0.5f).setDuration(800);
                ObjectAnimator transX = ObjectAnimator.ofFloat(tvGoldValue, "translationX", 0, originLocation[0] - currentLocation[0]).setDuration(800);
                ObjectAnimator transY = ObjectAnimator.ofFloat(tvGoldValue, "translationY", 0f, originLocation[1] - currentLocation[1]).setDuration(800);
                //                AnimatorSet set = new AnimatorSet();
                //                set.play(smallToBigX).with(smallToBigY);
                //                AnimatorSet transSet = new AnimatorSet();
                //                transSet.playTogether(bigToSmallX, bigToSmallY, transX, transY);
                //                anims.play(set).before(transSet);
                anims.playTogether(transX, transY);
                content.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anims.start();
                    }
                }, 1000);
                anims.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    
                    }
                    
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        content.removeView(view);
                    }
                    
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    
                    }
                });
            }
        });
    }
    
    /**
     * 回答问题奖励
     *
     * @param isCorrect 答题是否正确
     */
    public void showAnswerResult(Context context, int gold, int progress, boolean isCorrect) {
        final FrameLayout content = ((Activity) context).findViewById(android.R.id.content);
        final View view = LayoutInflater.from(context).inflate(R.layout.layout_answer_result, null);
        content.addView(view);
        final LottieAnimationView lottie = view.findViewById(R.id.lottieAnimationView);
        TextView tvValue = view.findViewById(R.id.tv_goldAndProgress);
        //        lottie.setScale(2 / context.getResources().getDisplayMetrics().density);
        lottie.setImageAssetsFolder("image/");
        ViewGroup.LayoutParams layoutParams = lottie.getLayoutParams();
        layoutParams.height = (int) (Math.min(content.getResources().getDisplayMetrics().heightPixels * 0.35f, content.getResources().getDisplayMetrics().widthPixels * 0.35f));
        lottie.setLayoutParams(layoutParams);
        //        lottie.setScale(layoutParams.height / 300);
        if (isCorrect) {
            lottie.setAnimation("correct.json");
        } else {
            lottie.setAnimation("wrong.json");
        }
        lottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            
            }
            
            @Override
            public void onAnimationEnd(Animator animation) {
                lottie.cancelAnimation();
                content.removeView(view);
            }
            
            @Override
            public void onAnimationCancel(Animator animation) {
            
            }
            
            @Override
            public void onAnimationRepeat(Animator animation) {
            
            }
        });
        lottie.playAnimation();
        tvValue.setText("金币 +" + gold + "  成长值 +" + progress);
        //        view.postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                content.removeView(view);
        //            }
        //        }, 3000);
    }
    
    private UltraViewPager mUltraViewPager;
    
    /**
     * 展示榜单
     *
     * @param context
     * @param lists
     */
    public void showRankList(Context context, List<ListStudent> lists) {
        final FrameLayout container = ((Activity) context).findViewById(android.R.id.content);
        View isView = container.findViewWithTag("rankList");
        if (isView != null && isView instanceof UltraViewPager) {
            mUltraViewPager = (UltraViewPager) isView;
        } else {
            mUltraViewPager = new UltraViewPager(context);
            mUltraViewPager.setTag("rankList");
            container.addView(mUltraViewPager);
        }
        
        mUltraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        LiveRoomAdapter liveRoomAdapter = new LiveRoomAdapter(context, lists, new ICallBack<String, String>() {
            @Override
            public String onPrev(String s) {
                return null;
            }
            
            @Override
            public void call(String s, int p) {
                container.removeView(mUltraViewPager);
                mUltraViewPager = null;
            }
        });
        mUltraViewPager.setAdapter(liveRoomAdapter);
        mUltraViewPager.setMultiScreen(0.6f);
        mUltraViewPager.setItemRatio(1.0);
        mUltraViewPager.setRatio(2.0f);
        //        mUltraViewPager.setMaxHeight(800);
        mUltraViewPager.setAutoMeasureHeight(true);
        //        if (style == 5) {
        mUltraViewPager.setPageTransformer(false, new UltraScaleTransformer());
        //        }
        //        if (style == 6) {
        //            ultraViewPager.setPageTransformer(false, new UltraDepthScaleTransformer());
        //        }
        
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mUltraViewPager.setLayoutParams(layoutParams);
    }
    
    /**
     * @param context
     */
    public <T> void showRightList(final Context context, final View clickView, final List<T> list, final ICallBack<T, String> iCallBack) {
        if (list == null) {
            return;
        }
        clickView.setEnabled(false);
        final FrameLayout content = ((Activity) context).findViewById(android.R.id.content);
        final View view = LayoutInflater.from(context).inflate(R.layout.layout_list, null);
        View flv = view.findViewById(R.id.fl_menu);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        view.findViewById(R.id.v_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.removeView(view);
                clickView.setEnabled(true);
            }
        });
        final int[] selectedPosition = {0};
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_lv_text_livemenu, null);
                return new Holder(view);
            }
            
            @Override
            public void onBindViewHolder(@NonNull final Holder holder, final int position) {
                holder.mTv_text.setSelected(false);
                holder.mTv_text.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
                String txt = iCallBack.onPrev(list.get(position));
                String currentTxt = "";
                holder.mTv_text.setText(txt);
                if (clickView instanceof TextView) {
                    currentTxt = ((TextView) clickView).getText().toString().trim();
                    if (txt != null && txt.equalsIgnoreCase(currentTxt)) {
                        holder.mTv_text.setSelected(true);
                        holder.mTv_text.setTextColor(ContextCompat.getColor(view.getContext(), R.color.color_75C82B));
                    }
                }
                holder.mTv_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedPosition[0] = position;
                        if (clickView instanceof TextView) {
                            ((TextView) clickView).setText(iCallBack.onPrev(list.get(position)));
                            holder.mTv_text.setSelected(true);
                        }
                        iCallBack.call(list.get(position), position);
                        content.removeView(view);
                        clickView.setEnabled(true);
                    }
                });
            }
            
            @Override
            public int getItemCount() {
                return list.size();
            }
        });
        content.addView(view);
        ObjectAnimator.ofFloat(flv, "translationX", content.getResources().getDimension(R.dimen.dp_200), 0).setDuration(400).start();
        
    }
    
    class Holder extends RecyclerView.ViewHolder {
        TextView mTv_text;
        
        public Holder(View itemView) {
            super(itemView);
            mTv_text = itemView.findViewById(R.id.text);
        }
    }
    
    /**
     * 清除所有dialog
     *
     * @param parent
     */
    public void clearParent(ViewGroup parent) {
        if (parent.getChildCount() > 0) {
            parent.removeAllViews();
        }
        parent.setVisibility(View.GONE);
        //判断是否有榜单，有的话清除
        FrameLayout container = ((Activity) parent.getContext()).findViewById(android.R.id.content);
        View isView = container.findViewWithTag("rankList");
        if (isView != null) {
            container.removeView(isView);
            mUltraViewPager = null;
        }
        
    }
    
    Timer mTimer;
    long count = 0;
    
    private void startTimeCount(final long time, final ACallBack<Long> callBack) {
        if (time > 0) {
            if (mTimer == null) {
                mTimer = new Timer();
            }
            count = time;
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    count--;
                    if (callBack != null) {
                        callBack.call(count);
                    }
                    if (count <= 0) {
                        mTimer.cancel();
                    }
                }
            }, 1000, 1000);
        }
    }
    
    /**
     * 圆形选项
     *
     * @param context
     * @param text
     * @return
     */
    private RadioButton newChoiceRadioButton(Context context, String text) {
        //    <RadioButton
        //        android:id="@+id/tv_ansA"
        //        android:layout_width="wrap_content"
        //        android:layout_height="wrap_content"
        //        android:background="@drawable/circle_answerabc"
        //        android:button="@null"
        //        android:gravity="center"
        //        android:text="A"
        //        android:textColor="@color/color_333333"
        //        android:textSize="20dp" />
        RadioButton rb = new RadioButton(context);
        rb.setButtonDrawable(null);
        rb.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_answerabc));
        rb.setGravity(Gravity.CENTER);
        rb.setText(text);
        rb.setPadding(5, 5, 5, 5);
        rb.setTextColor(ContextCompat.getColor(context, R.color.color_333333));
        rb.setTextSize(DensityUtil.dip2px(context, 6));
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(-2, -2);
        lp.leftMargin = DensityUtil.dip2px(context, 6);
        lp.rightMargin = DensityUtil.dip2px(context, 6);
        rb.setLayoutParams(lp);
        return rb;
    }
    
    /**
     * 长条选择题
     *
     * @param context
     * @param text
     * @return
     */
    private RadioButton newRadioButton(Context context, String choose, String text) {
        RadioButton rb = new RadioButton(context);
        rb.setButtonDrawable(null);
        rb.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_answerabc));
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(-1, -2);
        int left = DensityUtil.dip2px(context, 10);
        int top = DensityUtil.dip2px(context, 4);
        rb.setPadding(left, top, left, top);
        rb.setText(choose + "  " + text);
        rb.setTextColor(ContextCompat.getColor(context, R.color.color_181818));
        rb.setTextSize(DensityUtil.dip2px(context, 6));
        //        rb.setPadding();
        lp.topMargin = DensityUtil.dip2px(context, 2);
        lp.bottomMargin = DensityUtil.dip2px(context, 2);
        rb.setLayoutParams(lp);
        return rb;
    }
}
