package com.alex.e.mycalculator.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.e.mycalculator.R;
import com.alex.e.mycalculator.base.BaseActivity;
import com.alex.e.mycalculator.ui.stickylistheaders.StickyListHeadersAdapter;
import com.alex.e.mycalculator.ui.stickylistheaders.StickyListHeadersListView;
import com.alex.e.mycalculator.ui.widge.HintPopupWindow;
import com.alex.e.mycalculator.utils.Calculator;
import com.alex.e.mycalculator.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class DatabaseActivity extends BaseActivity {

    @BindView(R.id.gv_content)
    StickyListHeadersListView mGvMain;
    private List<Header> mData = new ArrayList<>();
    private MyAdapter mAdapter;

    public static void launch(Context from) {
        Intent intent = new Intent(from, DatabaseActivity.class);
        from.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.act_base;
    }

    @Override
    protected void initView() {
        super.initView();
        mAdapter = new MyAdapter();
        mGvMain.setAdapter(mAdapter);
        mGvMain.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                int i;
                for (i = 0; i < mData.size(); ++i) {
                    if (mData.get(i).headerId == headerId) {
                        break;
                    }
                }
                final int pos = i;
                new AlertDialog.Builder(mContext).setMessage("是否要删除 " + mData.get(i).header + "的数据???")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeDayData(pos);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });
        mGvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                View dialog = LayoutInflater.from(mContext).inflate(R.layout.ui_bottomsheetdialog, null);
                TextView detail = (TextView) dialog.findViewById(R.id.tv_detail);
                TextView delete = (TextView) dialog.findViewById(R.id.tv_delete);
                final BottomSheetDialog sheetDialog = new BottomSheetDialog(mContext);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(position);
                        mAdapter.notifyDataSetChanged();
                        sheetDialog.dismiss();
                    }
                });

                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataDetailActivity.launch(mContext, getItem(position), DataDetailActivity.TYPE_ONE_ITEM);
                        sheetDialog.dismiss();
                    }
                });
                sheetDialog.setCanceledOnTouchOutside(true);
                sheetDialog.setContentView(dialog);
                sheetDialog.show();
                return true;
            }
        });
    }

    //xx年xx月xx日
    private void removeDayData(int pos) {
        SPUtils spUtils = new SPUtils("DateList");
        spUtils.remove(mData.get(pos).header);
        SPUtils dataSP = new SPUtils(mData.get(pos).header);
        dataSP.clear();

        mData.remove(pos);
        mAdapter.notifyDataSetChanged();
    }

    private String getItem(int position) {
        int index = 0;
        for (int i = 0; i < mData.size(); ++i) {
            List<String> items = mData.get(i).items;
            for (int j = 0; j < items.size(); ++j) {
                if (position == index) {
                    return items.get(j);
                }
                index++;
            }
        }
        return null;
    }

    private void removeItem(int position) {
        int index = 0;
        for (int i = 0; i < mData.size(); ++i) {
            List<String> items = mData.get(i).items;
            for (int j = 0; j < items.size(); ++j) {
                if (position == index) {
                    SPUtils dateSP = new SPUtils("DateList");

                    SPUtils dataSP = new SPUtils(mData.get(i).header);
                    String dataList = dataSP.getString("data_list");
                    if (dataList == null) {
                        dataSP.clear();
                    } else {
                        String[] strings = dataList.split("@");
                        String temp = "";
                        for (int k = 0; k < strings.length; ++k) {
                            if (strings[k].contains(mData.get(i).header)) {
                                if (strings.length == 1) {
                                    dataSP.clear();
                                    removeDate(strings[k]);
                                    break;
                                }
                            } else {
                                temp += (TextUtils.isEmpty(temp) ? "" : "@") + strings[k];
                            }
                        }
                        if (!TextUtils.isEmpty(temp)) {
                            dataSP.putString(mData.get(i).header, temp);
                        }
                    }
                    mData.get(i).items.remove(j);
                    if (mData.get(i).items.size() == 0) {
                        mData.remove(i);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                }
                index++;
            }
        }
    }

    private void removeDate(String date) {
        SPUtils dateSp = new SPUtils("DateList");
        String dateList = dateSp.getString("date_list");
        if (dateList != null) {
            String temp = "";
            String[] strings = dateList.split("@");
            for (int i = 0; i < strings.length; ++i) {
                if (strings.equals(date)) {
                    continue;
                } else {
                    temp += (TextUtils.isEmpty(temp) ? "" : "@") + strings[i];
                }
            }
            dateSp.putString("date_list", temp);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        SPUtils spUtils = new SPUtils("DateList");
        String dateList = spUtils.getString("date_list");
        if (dateList == null) {
            Toast.makeText(mContext, "没有数据！！！", Toast.LENGTH_LONG);
            return;
        }

        String[] dates = dateList.split("@");
        for (int i = 0; i < dates.length; ++i) {
            SPUtils sp = new SPUtils(dates[i]);
            String dataList = sp.getString("data_list");
            if (dataList == null) {
                sp.clear();
                continue;
            }
            String[] datas = dataList.split("@");
            Header header = new Header();
            header.headerId = i;
            header.header = dates[i];
            for (int j = 0; j < datas.length; ++j) {
                header.items.add(datas[j]);
            }
            mData.add(header);
        }
    }

    private String oper(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        char ch = ' ';
        String temp = "";
        String arg1 = "";
        for (int i = 0; i < str.length(); ++i) {

            switch (str.charAt(i)) {
                case '+':
                case '/':
                case '*':
                case '-':
                    if (!TextUtils.isEmpty(arg1)) {
                        arg1 = Calculator.calculate(arg1, temp, ch + "");
                    } else {
                        arg1 = temp;
                    }
                    temp = "";
                    ch = str.charAt(i);
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.':
                    temp += str.charAt(i);
                    break;
            }
        }
        if (!TextUtils.isEmpty(temp) && ch != ' ') {
            arg1 = Calculator.calculate(arg1, temp, ch + "");
        }
        return arg1;
    }

    private Header getHeaderItem(int position) {
        int index = 0;
        for (int i = 0; i < mData.size(); ++i) {
            List<String> items = mData.get(i).items;
            for (int j = 0; j < items.size(); ++j) {
                if (position == index) {
                    return mData.get(i);
                }
                index++;
            }
        }
        return null;
    }
    HintPopupWindow hintPopupWindow;

    private class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        @Override
        public View getHeaderView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_database_header, null);
            }
            TextView header = (TextView) convertView.findViewById(R.id.tv_header);
            ImageView more = (ImageView) convertView.findViewById(R.id.iv_more);
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> list = new ArrayList<String>();
                    list.add("1.详情");
                    list.add("2.删除");
                    List<View.OnClickListener> clicklist = new ArrayList<View.OnClickListener>();
                    clicklist.add(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataDetailActivity.launch(mContext, getHeaderItem(position).header, DataDetailActivity.TYPE_ONE_DAY);
                            hintPopupWindow.dismissPopupWindow();
                        }
                    });
                    clicklist.add(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeDayData(position);
                            hintPopupWindow.dismissPopupWindow();
                        }
                    });
                    hintPopupWindow = new HintPopupWindow((Activity) mContext, list, clicklist);
                    hintPopupWindow.showPopupWindow(v);
                }
            });

            header.setText(getHeaderItem(position).header);
            return convertView;
        }



        @Override
        public long getHeaderId(int position) {
            return getHeaderItem(position).headerId;
        }

        @Override
        public int getCount() {
            int count = 0;
            for (int i = 0; i < mData.size(); ++i) {
                List<String> items = mData.get(i).items;
                count += items.size();
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            int index = 0;
            for (int i = 0; i < mData.size(); ++i) {
                List<String> items = mData.get(i).items;
                for (int j = 0; j < items.size(); ++j) {
                    if (position == index) {
                        return items.get(j);
                    }
                    index++;
                }
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_database, null);
            }
            String res = (String) getItem(position);
            res += " = " + oper(res);
            TextView item = (TextView) convertView.findViewById(R.id.tv_item);
            item.setText(res);
            return convertView;
        }

    }


    private class Header {
        public String header;
        public long headerId; // 从0开始自加；
        public List<String> items = new ArrayList<>();

    }
}
