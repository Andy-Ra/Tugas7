package com.andyra.tugas7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Tampil_Data extends Fragment{
    DatabaseAdapter mdbadapter;
    TextView kosong, tlno, tlnama, tljenis, tlstok;
    ListView mlistview;
    ArrayList<BarangModel> data_barang = new ArrayList<BarangModel>();

    private Context mctx;
    private int iid, inama, ijenis, istok;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tampil_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.daftar_barang);
        kosong = getActivity().findViewById(R.id.lv_null);
        mlistview = getActivity().findViewById(R.id.lvbarang);
        kosong.setVisibility(View.GONE);
        mlistview.setVisibility(View.GONE);

        mdbadapter = new DatabaseAdapter(getActivity());
        mdbadapter.open();

        Cursor mcursor = mdbadapter.tampil_barang();

        iid = mcursor.getColumnIndex("id_barang");
        inama = mcursor.getColumnIndex("nama_barang");
        ijenis = mcursor.getColumnIndex("jenis_barang");
        istok = mcursor.getColumnIndex("stok_barang");

        if(mcursor.getCount()==0){
            kosong.setVisibility(View.VISIBLE);
        }
        else{
            if (mcursor.moveToFirst()) {
                do {
                    data_barang.add(new BarangModel(
                            mcursor.getInt(iid),
                            mcursor.getString(inama),
                            mcursor.getString(ijenis),
                            mcursor.getInt(istok)));
                } while (mcursor.moveToNext());
            }
            mlistview.setVisibility(View.VISIBLE);
            mlistview.setAdapter(new tampil_data(data_barang));
        }

    }

    private class tampil_data extends BaseAdapter {
        //untuk mengambil nilai dari sqlite
        // dan meletakkan di array

        ArrayList<BarangModel> tampil_barang;
        public tampil_data(ArrayList<BarangModel> consdata_barang) {
            this.tampil_barang = consdata_barang;
        }
        @Override
        public int getCount() {
            return data_barang.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int posisi, View mview, ViewGroup parent) {
            LayoutInflater dataInflater = getLayoutInflater();
            View mcell = dataInflater.inflate(R.layout.list_barang, null);

            final BarangModel listdata = tampil_barang.get(posisi);
            tlno = mcell.findViewById(R.id.tl_no);
            tlnama = mcell.findViewById(R.id.tl_nama);
            tljenis = mcell.findViewById(R.id.tl_jenis);
            tlstok = mcell.findViewById(R.id.tl_stok);

            tlno.setText(String.valueOf(posisi+1));
            tlnama.setText(listdata.mdnama);
            tljenis.setText(listdata.mdjenis);
            tlstok.setText(String.valueOf(listdata.mdstok));
            return mcell;
        }
    }
}