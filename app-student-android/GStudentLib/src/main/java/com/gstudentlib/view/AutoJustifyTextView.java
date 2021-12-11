package com.gstudentlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.gstudentlib.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AutoJustifyTextView ————增强版的TextView，具有以下功能：
 */
public class AutoJustifyTextView extends TextView {

    private int mTextHighlightColor;// 选中文字背景高亮颜色
    private boolean isTextJustify = true;              // 是否需要两端对齐 ，默认true
    private int mViewTextWidth;         // SelectableTextView内容的宽度(不包含padding)

    public AutoJustifyTextView(Context context) {
        this(context, null);
    }

    public AutoJustifyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoJustifyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.AutoJustifyTextView);
        isTextJustify = mTypedArray.getBoolean(R.styleable.AutoJustifyTextView_textJustify, true);
        mTextHighlightColor = mTypedArray.getColor(R.styleable.AutoJustifyTextView_textHeightColor, 0x60ffeb3b);
        mTypedArray.recycle();
        init();
    }

    private void init() {
        if (isTextJustify)
            setGravity(Gravity.TOP);

        setTextHighlightColor(mTextHighlightColor);
    }

    public void setTextHighlightColor(int color) {
        this.mTextHighlightColor = color;
        String color_hex = String.format("%08X", color);
        color_hex = "#40" + color_hex.substring(2);
        setHighlightColor(Color.parseColor(color_hex));
    }

    // 两端对齐部分
    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("SelectableTextView", "onDraw");
        if (!isTextJustify) {
            // 不需要两端对齐
            super.onDraw(canvas);
        } else {
            //textview内容的实际宽度
            mViewTextWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            // 重绘文字，两端对齐
            drawTextWithJustify(canvas);
        }
    }

    /**
     * 重绘文字，两端对齐
     *
     * @param canvas
     */
    private void drawTextWithJustify(Canvas canvas) {
        // 文字画笔
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        String text_str = getText().toString();
        // 当前所在行的Y向偏移
        int currentLineOffsetY = getPaddingTop();
        currentLineOffsetY += getTextSize();

        Layout layout = getLayout();

        //循环每一行,绘制文字
        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            //获取到TextView每行中的内容
            String line_str = text_str.substring(lineStart, lineEnd);
            // 获取每行字符串的宽度(不包括字符间距)
            float desiredWidth = StaticLayout.getDesiredWidth(text_str, lineStart, lineEnd, getPaint());

            if (isLineNeedJustify(line_str)) {
                //最后一行不需要重绘
                if (i == layout.getLineCount() - 1) {
                    canvas.drawText(line_str, getPaddingLeft(), currentLineOffsetY, textPaint);
                } else {
                    drawJustifyTextForLine(canvas, line_str, desiredWidth, currentLineOffsetY);
                }
            } else {
                canvas.drawText(line_str, getPaddingLeft(), currentLineOffsetY, textPaint);
            }
            //更新行Y向偏移
            currentLineOffsetY += getLineHeight();
        }
    }

    /**
     * 重绘此行,两端对齐
     *
     * @param canvas
     * @param line_str           该行所有的文字
     * @param desiredWidth       该行每个文字的宽度的总和
     * @param currentLineOffsetY 该行的Y向偏移
     */
    private void drawJustifyTextForLine(Canvas canvas, String line_str, float desiredWidth, int currentLineOffsetY) {

        // 画笔X方向的偏移
        float lineTextOffsetX = getPaddingLeft();
        // 判断是否是首行
        if (isFirstLineOfParagraph(line_str)) {
            String blanks = "  ";
            // 画出缩进空格
            canvas.drawText(blanks, lineTextOffsetX, currentLineOffsetY, getPaint());
            // 空格需要的宽度
            float blank_width = StaticLayout.getDesiredWidth(blanks, getPaint());
            // 更新画笔X方向的偏移
            lineTextOffsetX += blank_width;
            line_str = line_str.substring(3);
        }

        // 计算相邻字符(或单词)之间需要填充的宽度,英文按单词处理，中文按字符处理
        // (TextView内容的实际宽度 - 该行字符串的宽度)/（字符或单词个数-1）
        if (isContentABC(line_str)) {
            // 该行包含英文,以空格分割单词
            String[] line_words = line_str.split(" ");
            // 计算相邻单词间需要插入的空白
            float insert_blank = mViewTextWidth - desiredWidth;
            if (line_words.length > 1)
                insert_blank = (mViewTextWidth - desiredWidth) / (line_words.length - 1);
            // 遍历单词
            for (int i = 0; i < line_words.length; i++) {
                // 判断分割后的每一个单词；如果是纯英文，按照纯英文单词处理，直接在画布上画出单词；
                // 如果包括汉字，则按照汉字字符处理，逐个字符绘画
                // 如果只有一个单词，按中文处理
                // 最后一个单词按照纯英文单词处理
                String word_i = line_words[i] + " ";
                if (line_words.length == 1 || (isContentHanZi(word_i) && i < line_words.length - 1)) {
                    // 单词按照汉字字符处理
                    // 计算单词中相邻字符间需要插入的空白
                    float insert_blank_word_i = insert_blank;
                    if (word_i.length() > 1)
                        insert_blank_word_i = insert_blank / (word_i.length() - 1);
                    // 遍历单词中字符，依次绘画
                    for (int j = 0; j < word_i.length(); j++) {
                        String word_i_char_j = String.valueOf(word_i.charAt(j));
                        float word_i_char_j_width = StaticLayout.getDesiredWidth(word_i_char_j, getPaint());
                        canvas.drawText(word_i_char_j, lineTextOffsetX, currentLineOffsetY, getPaint());
                        // 更新画笔X方向的偏移
                        lineTextOffsetX += word_i_char_j_width + insert_blank_word_i;
                    }
                } else {
                    //单词按照纯英文处理
                    float word_i_width = StaticLayout.getDesiredWidth(word_i, getPaint());
                    canvas.drawText(word_i, lineTextOffsetX, currentLineOffsetY, getPaint());
                    // 更新画笔X方向的偏移
                    lineTextOffsetX += word_i_width + insert_blank;
                }
            }
        } else {
            // 该行按照中文处理
            float insert_blank = (mViewTextWidth - desiredWidth) / (line_str.length() - 1);
            for (int i = 0; i < line_str.length(); i++) {
                String char_i = String.valueOf(line_str.charAt(i));
                float char_i_width = StaticLayout.getDesiredWidth(char_i, getPaint());
                canvas.drawText(char_i, lineTextOffsetX, currentLineOffsetY, getPaint());
                // 更新画笔X方向的偏移
                lineTextOffsetX += char_i_width + insert_blank;
            }
        }
    }

    /**
     * 判断是不是段落的第一行。一个汉字相当于一个字符，此处判断是否为第一行的依据是：
     * 字符长度大于3且前两个字符为空格
     *
     * @param line
     * @return
     */
    private boolean isFirstLineOfParagraph(String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    /**
     * 判断该行需不需要缩放；该行最后一个字符不是换行符的时候返回true，
     * 该行最后一个字符是换行符的时候返回false
     *
     * @param line_str 该行的文字
     * @return
     */
    private boolean isLineNeedJustify(String line_str) {
        if (line_str.length() == 0) {
            return false;
        } else {
            return line_str.charAt(line_str.length() - 1) != '\n';
        }
    }

    /**
     * 判断是否包含英文
     *
     * @param line_str
     * @return
     */
    private boolean isContentABC(String line_str) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(line_str);
        return m.matches();
    }

    /**
     * 判断是否包含中文
     *
     * @param word_str
     * @return
     */
    private boolean isContentHanZi(String word_str) {
        String regex = ".*[\\u4e00-\\u9fa5]+.*";
        Matcher m = Pattern.compile(regex).matcher(word_str);
        return m.matches();
    }

}
