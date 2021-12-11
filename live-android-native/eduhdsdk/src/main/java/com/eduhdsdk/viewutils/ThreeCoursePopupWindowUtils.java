package com.eduhdsdk.viewutils;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.classroomsdk.WhiteBoradConfig;
import com.classroomsdk.bean.ShareDoc;
import com.eduhdsdk.R;
import com.eduhdsdk.adapter.ThreeFileListAdapter;
import com.eduhdsdk.adapter.ThreeMediaListAdapter;
import com.eduhdsdk.comparator.NameComparator;
import com.eduhdsdk.comparator.TimeComparator;
import com.eduhdsdk.comparator.TypeComparator;
import com.eduhdsdk.message.RoomControler;
import com.eduhdsdk.tools.FullScreenTools;
import com.eduhdsdk.tools.ScreenScale;
import com.eduhdsdk.tools.Tools;
import com.talkcloud.room.RoomUser;
import com.talkcloud.room.TKRoomManager;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Administrator on 2018/4/20.
 * 课件库
 */

public class ThreeCoursePopupWindowUtils implements View.OnClickListener {

    private Activity activity;
    private PopupWindow popupWindowCourse;
    private RelativeLayout ll_course_list;
    private LinearLayout ll_media_list, ll_temp;
    private ListView lv_course_data;
    private ListView lv_media_data;
    private TextView tv_popup_title;
    private ArrayList<RoomUser> memberList;
    private ThreeMediaListAdapter mediaListAdapter;
    private ThreeFileListAdapter fileListAdapter;

    private PopupWindowClick popup_click;
    private TextView file_tv_class, file_tv_admin, media_tv_class, media_tv_admin;
    private LinearLayout file_classifi, media_classifi;
    private ImageView iv_media_time_sort, iv_media_type_sort, iv_media_name_sort,
            iv_file_time_sort, iv_file_type_sort, iv_file_name_sort;


    private LinearLayout ll_course_library, ll_media;
    private TextView tv_courselibrary_name, tv_media_name;
    private View view_courselibrary_point, view_media_point;

    private boolean isFlageFile = false;
    private boolean isFlageMedia = false;

    /**
     * 记录时间，类型，名称的排序状态      0 正常  1 上三角被选中  2  下三角被选中
     */
    int media_time_status = 0, media_type_status = 0, media_name_status = 0;
    int file_time_status = 0, file_type_status = 0, file_name_status = 0;

    public ThreeCoursePopupWindowUtils(Activity activity, ArrayList<RoomUser> memberList, String media_serial, String file_serial) {
        isFlageMedia = false;
        isFlageMedia = false;
        this.activity = activity;
        this.memberList = memberList;
        mediaListAdapter = new ThreeMediaListAdapter(activity, media_serial);
        fileListAdapter = new ThreeFileListAdapter(activity, file_serial);
    }

    public ThreeMediaListAdapter getMediaListAdapter() {
        return this.mediaListAdapter;
    }

    public ThreeFileListAdapter getFileListAdapter() {
        return this.fileListAdapter;
    }

    public void setPopupWindowClick(PopupWindowClick popup_click) {
        this.popup_click = popup_click;
    }

    private View popup_window_view;

    /**
     * 预加载popupwindow的view
     * <p>
     */
    public void initCoursePopupWindow() {

        if (popupWindowCourse != null) {
            return;
        }

        View contentView = LayoutInflater.from(activity).inflate(R.layout.three_layout_course_popupwindow, null);

        ScreenScale.scaleView(contentView, "CoursePopupWindowUtils");

        ll_course_library = (LinearLayout) contentView.findViewById(R.id.ll_course_library);
        ll_media = (LinearLayout) contentView.findViewById(R.id.ll_media);
        tv_courselibrary_name = (TextView) contentView.findViewById(R.id.tv_courselibrary_name);
        tv_media_name = (TextView) contentView.findViewById(R.id.tv_media_name);
        view_courselibrary_point = contentView.findViewById(R.id.view_courselibrary_point);
        view_media_point = contentView.findViewById(R.id.view_media_point);

        ll_course_library.setOnClickListener(this);
        ll_media.setOnClickListener(this);

        ll_course_list = (RelativeLayout) contentView.findViewById(R.id.ll_course_list);
        ll_media_list = (LinearLayout) contentView.findViewById(R.id.ll_media_list);

        ll_temp = (LinearLayout) contentView.findViewById(R.id.ll_temp);

        if (TKRoomManager.getInstance().getMySelf().role == 4) {
            ll_temp.setVisibility(View.GONE);
        }

        LinearLayout popup_file_title_layout = (LinearLayout) contentView.findViewById(R.id.popup_file_title_layout);
        file_tv_class = (TextView) popup_file_title_layout.findViewById(R.id.tv_class);
        file_tv_admin = (TextView) popup_file_title_layout.findViewById(R.id.tv_admin);
        file_classifi = (LinearLayout) popup_file_title_layout.findViewById(R.id.ll_classifi);
        file_tv_admin.setBackgroundResource(R.color.nothing);
        file_tv_class.setTextAppearance(activity, R.style.tv_class_checked);

        iv_file_time_sort = (ImageView) popup_file_title_layout.findViewById(R.id.iv_time_sort);
        iv_file_type_sort = (ImageView) popup_file_title_layout.findViewById(R.id.iv_type_sort);
        iv_file_name_sort = (ImageView) popup_file_title_layout.findViewById(R.id.iv_name_sort);

        LinearLayout ll_file_time_sort = (LinearLayout) popup_file_title_layout.findViewById(R.id.ll_time_sort);
        LinearLayout ll_file_type_sort = (LinearLayout) popup_file_title_layout.findViewById(R.id.ll_type_sort);
        LinearLayout ll_file_name_sort = (LinearLayout) popup_file_title_layout.findViewById(R.id.ll_name_sort);

        ll_file_time_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (file_time_status) {
                    case 1:  //时间正序
                        sortTime(false, fileListAdapter.getArrayList(), 0);
                        iv_file_time_sort.setImageResource(R.drawable.three_arrange_down);

                        file_time_status = 2;
                        break;
                    case 2:   //时间倒序
                        sortTime(true, fileListAdapter.getArrayList(), 0);
                        iv_file_time_sort.setImageResource(R.drawable.three_arrange_up);
                        file_time_status = 1;
                        break;
                }
                file_type_status = 1;
                file_name_status = 1;
                iv_file_name_sort.setImageResource(R.drawable.three_arrange_none);
                iv_file_type_sort.setImageResource(R.drawable.three_arrange_none);

            }
        });

        ll_file_type_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (file_type_status) {
                    case 1:    //类型正序
                        sortType(true, fileListAdapter.getArrayList(), 0);
                        iv_file_type_sort.setImageResource(R.drawable.three_arrange_down);
                        file_type_status = 2;
                        break;
                    case 2:    //类型倒序
                        sortType(false, fileListAdapter.getArrayList(), 0);
                        iv_file_type_sort.setImageResource(R.drawable.three_arrange_up);
                        file_type_status = 1;
                        break;
                }
                file_time_status = 1;
                file_name_status = 1;
                iv_file_name_sort.setImageResource(R.drawable.three_arrange_none);
                iv_file_time_sort.setImageResource(R.drawable.three_arrange_none);
            }
        });

        ll_file_name_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (file_name_status) {
                    case 1:   //名称倒序
                        sortName(true, fileListAdapter.getArrayList(), 0);
                        iv_file_name_sort.setImageResource(R.drawable.three_arrange_down);
                        file_name_status = 2;
                        break;
                    case 2:     //名称正序
                        sortName(false, fileListAdapter.getArrayList(), 0);
                        iv_file_name_sort.setImageResource(R.drawable.three_arrange_up);
                        file_name_status = 1;
                        break;
                }
                file_time_status = 1;
                file_type_status = 1;
                iv_file_time_sort.setImageResource(R.drawable.three_arrange_none);
                iv_file_type_sort.setImageResource(R.drawable.three_arrange_none);
            }
        });

        LinearLayout popup_media_title_layout = (LinearLayout) contentView.findViewById(R.id.popup_media_title_layout);
        media_tv_class = (TextView) popup_media_title_layout.findViewById(R.id.tv_class);
        media_tv_admin = (TextView) popup_media_title_layout.findViewById(R.id.tv_admin);
        media_classifi = (LinearLayout) popup_media_title_layout.findViewById(R.id.ll_classifi);
        media_tv_admin.setBackgroundResource(R.color.nothing);
        media_tv_class.setTextAppearance(activity, R.style.tv_class_checked);

        iv_media_time_sort = (ImageView) popup_media_title_layout.findViewById(R.id.iv_time_sort);
        iv_media_type_sort = (ImageView) popup_media_title_layout.findViewById(R.id.iv_type_sort);
        iv_media_name_sort = (ImageView) popup_media_title_layout.findViewById(R.id.iv_name_sort);
        LinearLayout ll_media_time_sort = (LinearLayout) popup_media_title_layout.findViewById(R.id.ll_time_sort);
        LinearLayout ll_media_type_sort = (LinearLayout) popup_media_title_layout.findViewById(R.id.ll_type_sort);
        LinearLayout ll_media_name_sort = (LinearLayout) popup_media_title_layout.findViewById(R.id.ll_name_sort);

        ll_media_time_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (media_time_status) {
                    case 1:  //时间正序
                        sortTime(false, mediaListAdapter.getArrayList(), 1);
                        iv_media_time_sort.setImageResource(R.drawable.three_arrange_down);
                        media_time_status = 2;
                        break;
                    case 2:   //时间倒序
                        sortTime(true, mediaListAdapter.getArrayList(), 1);
                        iv_media_time_sort.setImageResource(R.drawable.three_arrange_up);
                        media_time_status = 1;
                        break;
                }
                media_type_status = 1;
                media_name_status = 1;

                iv_media_name_sort.setImageResource(R.drawable.three_arrange_none);
                iv_media_type_sort.setImageResource(R.drawable.three_arrange_none);
            }
        });

        ll_media_type_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (media_type_status) {
                    case 1:    //类型正序
                        sortType(true, mediaListAdapter.getArrayList(), 1);
                        iv_media_type_sort.setImageResource(R.drawable.three_arrange_down);
                        media_type_status = 2;
                        break;
                    case 2:    //类型倒序
                        sortType(false, mediaListAdapter.getArrayList(), 1);
                        iv_media_type_sort.setImageResource(R.drawable.three_arrange_up);
                        media_type_status = 1;
                        break;
                }
                media_time_status = 1;
                media_name_status = 1;
                iv_media_name_sort.setImageResource(R.drawable.three_arrange_none);
                iv_media_time_sort.setImageResource(R.drawable.three_arrange_none);

            }
        });

        ll_media_name_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (media_name_status) {
                    case 1:   //名称倒序
                        sortName(true, mediaListAdapter.getArrayList(), 1);
                        iv_media_name_sort.setImageResource(R.drawable.three_arrange_down);
                        media_name_status = 2;
                        break;
                    case 2:     //名称正序
                        sortName(false, mediaListAdapter.getArrayList(), 1);
                        iv_media_name_sort.setImageResource(R.drawable.three_arrange_up);
                        media_name_status = 1;
                        break;
                }
                media_time_status = 1;
                media_type_status = 1;
                iv_media_time_sort.setImageResource(R.drawable.three_arrange_none);
                iv_media_type_sort.setImageResource(R.drawable.three_arrange_none);
            }
        });

        lv_course_data = (ListView) contentView.findViewById(R.id.lv_course_data);
        lv_media_data = (ListView) contentView.findViewById(R.id.lv_media_data);

        tv_popup_title = (TextView) contentView.findViewById(R.id.tv_popup_title);

        contentView.findViewById(R.id.iv_popup_close).setOnClickListener(this);
        contentView.findViewById(R.id.popup_take_photo).setOnClickListener(this);
        contentView.findViewById(R.id.popup_choose_photo).setOnClickListener(this);

        file_tv_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (popup_click != null) {
                    popup_click.choose_class_documents();
                    fileListAdapter.isPublicDocuments(false);
                    sortTime(false, fileListAdapter.getArrayList(), 0);

                    isFlageFile = false;
                    if (file_tv_class != null && file_tv_admin != null) {
                        file_tv_class.setTextAppearance(activity, R.style.tv_class_checked);
                        file_tv_class.setBackgroundResource(R.drawable.three_file_pressed_backgroud);
                        file_tv_admin.setBackgroundResource(R.color.nothing);
                        file_tv_admin.setTextAppearance(activity, R.style.tv_class_no_checked);
                    }
                }
            }
        });

        file_tv_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popup_click != null) {
                    isFlageFile = true;
                    popup_click.choose_admin_documents();
                    fileListAdapter.isPublicDocuments(true);
                    sortTime(false, fileListAdapter.getArrayList(), 0);
                    if (file_tv_class != null && file_tv_admin != null) {
                        file_tv_admin.setTextAppearance(activity, R.style.tv_class_checked);
                        file_tv_admin.setBackgroundResource(R.drawable.three_file_pressed_backgroud);
                        file_tv_class.setBackgroundResource(R.color.nothing);
                        file_tv_class.setTextAppearance(activity, R.style.tv_class_no_checked);
                    }
                }
            }
        });

        media_tv_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popup_click != null) {
                    isFlageMedia = false;
                    popup_click.choose_class_media();

                    mediaListAdapter.isPublicDocuments(false);
                    sortTime(false, mediaListAdapter.getArrayList(), 1);

                    if (media_tv_class != null && media_tv_admin != null) {
                        media_tv_class.setTextAppearance(activity, R.style.tv_class_checked);
                        media_tv_class.setBackgroundResource(R.drawable.three_file_pressed_backgroud);
                        media_tv_admin.setBackgroundResource(R.color.nothing);
                        media_tv_admin.setTextAppearance(activity, R.style.tv_class_no_checked);
                    }
                }
            }
        });

        media_tv_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popup_click != null) {
                    isFlageMedia = true;
                    popup_click.choose_admin_media();

                    mediaListAdapter.isPublicDocuments(true);
                    sortTime(false, mediaListAdapter.getArrayList(), 1);

                    if (media_tv_class != null && media_tv_admin != null) {
                        media_tv_class.setTextAppearance(activity, R.style.tv_class_no_checked);
                        media_tv_class.setBackgroundResource(R.color.nothing);
                        media_tv_admin.setTextAppearance(activity, R.style.tv_class_checked);
                        media_tv_admin.setBackgroundResource(R.drawable.three_file_pressed_backgroud);
                    }
                }
            }
        });
        changeBackground(true);
        popup_window_view = contentView;
    }


    //判断弹框弹出时，用户的点击是在底部控件的内部还是外部
    boolean isInView = true;

    /**
     * 弹出popupwindow
     *
     * @param view
     * @param cb_view
     * @param width
     * @param height
     * @param padLeft
     * @param webwidth
     * @param scale    是否是4：3 一对一专用
     */
    public void showCoursePopupWindow(View view, final View cb_view, int width, int height, boolean padLeft, int webwidth, boolean scale) {

        if (popup_window_view == null) {
            return;
        }

        popupWindowCourse = new PopupWindow(width, height);
        popupWindowCourse.setContentView(popup_window_view);

        lv_media_data.setAdapter(mediaListAdapter);
        lv_course_data.setAdapter(fileListAdapter);


        if (RoomControler.isDocumentClassification()) {
            file_classifi.setVisibility(View.VISIBLE);
            media_classifi.setVisibility(View.VISIBLE);
            //fileListAdapter.setArrayList(WhiteBoradManager.getInstance().getClassDocList());
            //mediaListAdapter.setArrayList(WhiteBoradManager.getInstance().getClassMediaList());
            if (!isFlageFile) {
                popup_click.choose_class_documents();
                changeBackground(true);
            } else {
                popup_click.choose_admin_documents();
                changeBackground(false);
            }

            if (!isFlageMedia) {
                changeBackground(true);
                popup_click.choose_class_media();
            } else {
                popup_click.choose_admin_media();
                changeBackground(false);
            }

        } else {
            file_classifi.setVisibility(View.INVISIBLE);
            media_classifi.setVisibility(View.INVISIBLE);
            fileListAdapter.setArrayList(WhiteBoradConfig.getsInstance().getDocList());
            mediaListAdapter.setArrayList(WhiteBoradConfig.getsInstance().getMediaList());
        }

        sortTime(false, fileListAdapter.getArrayList(), 0);
        iv_file_time_sort.setImageResource(R.drawable.three_arrange_down);
        iv_file_name_sort.setImageResource(R.drawable.three_arrange_none);
        iv_file_type_sort.setImageResource(R.drawable.three_arrange_none);

        file_time_status = 2;

        popupWindowCourse.setBackgroundDrawable(new BitmapDrawable());
        popupWindowCourse.setFocusable(false);
        popupWindowCourse.setOutsideTouchable(true);

        popupWindowCourse.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (popup_click != null) {
//                    if (!isInView) {
                    popup_click.close_window();
//                    }
                }
            }
        });

        popupWindowCourse.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isInView = Tools.isInView(event, cb_view);
                return false;
            }
        });

        fileListAdapter.setPop(popupWindowCourse);
        mediaListAdapter.setPop(popupWindowCourse);

        int[] reb_wb_board = new int[2];
        view.getLocationInWindow(reb_wb_board);

        //popupwindow基于屏幕左上角位移到给定view中心的偏移量
        int x = 0;
        if (padLeft) {
            x = Math.abs(view.getWidth() - popupWindowCourse.getWidth()) / 2 + FullScreenTools.getStatusBarHeight(activity) + webwidth;
        } else {
            x = Math.abs(view.getWidth() - popupWindowCourse.getWidth()) / 2 + webwidth;
        }
        if (scale) {
            if (padLeft) {
                x = FullScreenTools.getStatusBarHeight(activity);
                popupWindowCourse.setWidth(view.getWidth() + reb_wb_board[0] - FullScreenTools.getStatusBarHeight(activity));
            } else {
                x = 0;
                popupWindowCourse.setWidth(view.getWidth() + reb_wb_board[0]);
            }

        }
        int y = Math.abs(reb_wb_board[1] + view.getHeight() / 2 - popupWindowCourse.getHeight() / 2);
        popupWindowCourse.showAtLocation(view, Gravity.NO_GRAVITY, x, y);
    }

    public void dismissPopupWindow() {
        if (popupWindowCourse != null) {
            popupWindowCourse.dismiss();
        }
    }


    /**
     * @param b true：课件库 false：媒体库
     */
    public void changeBackground(boolean b) {
        if (b) {
            ll_course_library.setBackgroundResource(R.drawable.three_selector_library_default);
            view_courselibrary_point.setBackgroundResource(R.drawable.three_shape_popou_course_point_select);
            view_courselibrary_point.setVisibility(View.VISIBLE);
            tv_courselibrary_name.setTextColor(activity.getResources().getColor(R.color.three_popup_student_name_color));

            ll_media.setBackgroundResource(R.drawable.three_selector_library_select);
            view_media_point.setBackgroundResource(R.drawable.three_shape_popou_course_point_default);
            view_media_point.setVisibility(View.GONE);
            tv_media_name.setTextColor(activity.getResources().getColor(R.color.white));

            ll_course_list.setVisibility(View.VISIBLE);
            ll_media_list.setVisibility(View.GONE);
            tv_popup_title.setText(activity.getString(R.string.doclist) + "（" +
                    WhiteBoradConfig.getsInstance().getDocList().size() + "）");
        } else {
            ll_media.setBackgroundResource(R.drawable.three_selector_library_default);
            view_media_point.setBackgroundResource(R.drawable.three_shape_popou_course_point_select);
            view_media_point.setVisibility(View.VISIBLE);
            tv_media_name.setTextColor(activity.getResources().getColor(R.color.three_popup_student_name_color));

            ll_course_library.setBackgroundResource(R.drawable.three_selector_library_select);
            view_courselibrary_point.setBackgroundResource(R.drawable.three_shape_popou_course_point_default);
            view_courselibrary_point.setVisibility(View.GONE);
            tv_courselibrary_name.setTextColor(activity.getResources().getColor(R.color.white));


            ll_course_list.setVisibility(View.GONE);
            ll_media_list.setVisibility(View.VISIBLE);

            tv_popup_title.setText(activity.getString(R.string.medialist) + "（" +
                    WhiteBoradConfig.getsInstance().getMediaList().size() + "）");

            sortTime(false, mediaListAdapter.getArrayList(), 1);
            iv_media_time_sort.setImageResource(R.drawable.three_arrange_down);
            iv_media_name_sort.setImageResource(R.drawable.three_arrange_none);
            iv_media_type_sort.setImageResource(R.drawable.three_arrange_none);
            media_time_status = 2;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_popup_close) {
            if (popupWindowCourse != null) {
                popupWindowCourse.dismiss();
                if (popup_click != null) {
                    popup_click.close_window();
                }
            }
        } else if (v.getId() == R.id.popup_take_photo) {
            if (popup_click != null) {
                popup_click.take_photo();
            }
        } else if (v.getId() == R.id.popup_choose_photo) {
            if (popup_click != null) {
                popup_click.choose_photo();
            }
        } else if (v.getId() == R.id.ll_course_library) {//课件库
            changeBackground(true);

        } else if (v.getId() == R.id.ll_media) {//媒体库
            changeBackground(false);
        }
    }

    /**
     * 定义popupwindow的接口，通过接口和activity进行通信
     */
    public interface PopupWindowClick {
        void close_window();

        void take_photo();

        void choose_photo();

        void choose_class_documents();

        void choose_admin_documents();

        void choose_class_media();

        void choose_admin_media();
    }

    private void sortTime(Boolean isup, ArrayList<ShareDoc> arrayList, int type1) {
        ShareDoc shareDoc = null;
        for (int x = arrayList.size() - 1; x >= 0; x--) {
            if (arrayList.get(x).getFileid() == 0) {
                shareDoc = arrayList.get(x);
                arrayList.remove(x);
            }
        }
        TimeComparator timeComparator = new TimeComparator();
        timeComparator.setisUp(isup);
        Collections.sort(arrayList, timeComparator);
        if (shareDoc != null) {
            arrayList.add(0, shareDoc);
        }
        if (type1 == 0) {
            fileListAdapter.setArrayList(arrayList);
        } else {
            mediaListAdapter.setArrayList(arrayList);
        }
    }

    private void sortName(Boolean isup, ArrayList<ShareDoc> arrayList, int type1) {
        ShareDoc shareDoc = null;
        for (int x = arrayList.size() - 1; x >= 0; x--) {
            if (arrayList.get(x).getFileid() == 0) {
                shareDoc = arrayList.get(x);
                arrayList.remove(x);
            }
        }
        NameComparator nameComparator = new NameComparator();
        nameComparator.setisUp(isup);
        Collections.sort(arrayList, nameComparator);
        if (shareDoc != null) {
            arrayList.add(0, shareDoc);
        }
        if (type1 == 0) {
            fileListAdapter.setArrayList(arrayList);
        } else {
            mediaListAdapter.setArrayList(arrayList);
        }
    }

    private void sortType(Boolean isup, ArrayList<ShareDoc> arrayList, int type1) {

        ShareDoc shareDoc = null;
        for (int x = arrayList.size() - 1; x >= 0; x--) {
            if (arrayList.get(x).getFileid() == 0) {
                shareDoc = arrayList.get(x);
                arrayList.remove(x);
            }
        }
        TypeComparator typeComparator = new TypeComparator();
        typeComparator.setisUp(isup);
        Collections.sort(arrayList, typeComparator);
        if (shareDoc != null) {
            arrayList.add(0, shareDoc);
        }
        if (type1 == 0) {
            fileListAdapter.setArrayList(arrayList);
        } else {
            mediaListAdapter.setArrayList(arrayList);
        }
    }
}
