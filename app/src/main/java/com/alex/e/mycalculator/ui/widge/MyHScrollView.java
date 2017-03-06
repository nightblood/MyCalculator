package com.alex.e.mycalculator.ui.widge;

import java.util.ArrayList;
import java.util.List;

import com.example.excel_sorttabledemo.MainActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/*
 * �Զ����?�����ؼ�
 * ������ onScrollChanged���������仯��,����ÿ�εı仯֪ͨ�� �۲�(�˱仯��)�۲���
 * ��ʹ�� AddOnScrollChangedListener �����ı��ؼ��� �������仯
 * */
public class MyHScrollView extends HorizontalScrollView {
	private ScrollViewSubject mScrollViewSubject = new ScrollViewSubject();

	public MyHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyHScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.i("pdwy", "MyHScrollView dispatchTouchEvent2");
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		// return super.onInterceptTouchEvent(ev);
		Log.i("pdwy", "MyHScrollView onInterceptTouchEvent1");
		return super.onInterceptTouchEvent(ev);

		// return super.onInterceptTouchEvent(ev);
	}

	int x;
	int y;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = (int) ev.getX();
			// Host_view.isTouchItem = true;
			break;
		case MotionEvent.ACTION_MOVE:

			// Host_view.isTouchItem = false;
			break;
		case MotionEvent.ACTION_UP:
			y = (int) ev.getX();

			if (Math.abs(x - y) > 20) {
				MainActivity.isTouchItem = false;
			} else {
				MainActivity.isTouchItem = true;
			}
			break;
		}
		Log.i("pd", "MyHScrollView onTouchEvent3");
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		/*
		 * ���������ƶ����� �����¼���֪ͨ��۲��ߣ��۲��߻ᴫ�������ġ�?
		 */
		if (mScrollViewSubject != null /* && (l != oldl || t != oldt) */) {
			mScrollViewSubject.NotifyOnScrollChanged(l, t, oldl, oldt);
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

	/*
	 * ���� ���ؼ� �� �������仯�¼�
	 */
	public void AddScrollViewObserver(ScrollViewObserver observer) {
		mScrollViewSubject.AddScrollViewObserver(observer);
	}

	/*
	 * ȡ�� ���� ���ؼ� �� �������仯�¼�
	 */
	public void RemoveScrollViewObserver(ScrollViewObserver observer) {
		mScrollViewSubject.RemoveScrollViewObserver(observer);
	}

	/*
	 * �۲��� ----�������˹����¼�ʱ
	 */
	public static interface ScrollViewObserver {
		public void onScrollChanged(int l, int t, int oldl, int oldt);
	}

	/*
	 * ֪ͨ��
	 */
	public static class ScrollViewSubject {
		List<ScrollViewObserver> mList;

		public ScrollViewSubject() {
			// super();
			mList = new ArrayList<ScrollViewObserver>();
		}

		// ���ӹ۲���
		public void AddScrollViewObserver(ScrollViewObserver observer) {
			mList.add(observer);
		}

		// �Ƴ�۲���?
		public void RemoveScrollViewObserver(ScrollViewObserver observer) {
			mList.remove(observer);
		}

		// ֪ͨ
		public void NotifyOnScrollChanged(int l, int t, int oldl, int oldt) {
			if (mList == null || mList.size() == 0) {
				return;
			}
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i) != null) {
					mList.get(i).onScrollChanged(l, t, oldl, oldt);
				}
			}
		}
	}
}
