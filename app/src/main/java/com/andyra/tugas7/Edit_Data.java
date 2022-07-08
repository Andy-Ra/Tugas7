package com.andyra.tugas7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class Edit_Data extends Fragment {
    private TextInputLayout ed_jenis, ed_stok;
    String pid, pnama;
    Button btnedit;

    Spinner spednama;
    DatabaseAdapter mdbadapter;
    ArrayList<BarangModel> arrayambil = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_edit_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.edit_barang);
        ed_jenis = getActivity().findViewById(R.id.ed_edjenis);
        ed_stok = getActivity().findViewById(R.id.ed_edstok);
        spednama = getActivity().findViewById(R.id.sp_ednama);

        btnedit = getActivity().findViewById(R.id.btnedit);
        mdbadapter = new DatabaseAdapter(getActivity());
        mdbadapter.open();

        tampilnama();
        clear();
        pilih();
    }

    private void tampilnama(){
        ArrayList<String> anamabarang = new ArrayList<>();
        Cursor mcursor = mdbadapter.tampil_barang();
        int ibrgnama = mcursor.getColumnIndex("nama_barang");
        anamabarang.clear();
        anamabarang.add("Pilih Barang");
        if (mcursor.moveToFirst()) {
            do {
                anamabarang.add(mcursor.getString(ibrgnama));
            } while (mcursor.moveToNext());
        }
        ArrayAdapter<String> marrayadapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, anamabarang);
        spednama.setAdapter(marrayadapter);

        spednama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> madapterView, View view, int posisi, long l) {
                ambildata();
                if(posisi > 0 ){
                    final BarangModel listdata = arrayambil.get(posisi-1);

                    pid = String.valueOf(listdata.mdid);
                    pnama = listdata.mdnama;
                    ed_jenis.getEditText().setText(listdata.mdjenis);
                    ed_stok.getEditText().setText(String.valueOf(listdata.mdstok));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void ambildata() {
        mdbadapter.open();
        Cursor mcursor = mdbadapter.tampil_barang();
        if (mcursor.getCount() == 0) {
        } else {
            int ibrgid, ibrgnama, ibrgjenis, ibrgstok;
            ibrgid = mcursor.getColumnIndex("id_barang");
            ibrgnama = mcursor.getColumnIndex("nama_barang");
            ibrgjenis = mcursor.getColumnIndex("jenis_barang");
            ibrgstok = mcursor.getColumnIndex("stok_barang");
            if (mcursor.moveToFirst()) {
                do {
                    arrayambil.add( new BarangModel(
                            mcursor.getInt(ibrgid),
                            mcursor.getString(ibrgnama),
                            mcursor.getString(ibrgjenis),
                            mcursor.getInt(ibrgstok)));
                } while (mcursor.moveToNext());
            }
        }
        mdbadapter.close();
    }


    private void pilih(){
        Bundle mbundle = this.getArguments();
        String opsi = mbundle.getString("opsi");
        if (opsi.equals("edit")){
            getActivity().setTitle(R.string.edit_barang);
            btnedit.setText(R.string.edit_barang);
            ed_jenis.setEnabled(true);
            ed_stok.setEnabled(true);
        }
        else if(opsi.equals("delete")){
            getActivity().setTitle(R.string.hapus_barang);
            btnedit.setText(R.string.hapus_barang);
            ed_jenis.setEnabled(false);
            ed_stok.setEnabled(false);
        }
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear_notif();
                String jenis = ed_jenis.getEditText().getText().toString();
                String stok = ed_stok.getEditText().getText().toString();
                if (opsi.equals("edit")){
                    if( spednama.getSelectedItemId() == 0 || jenis.equals("") || stok.equals("") || !stok.matches("[0-9]+")){
                        if(spednama.getSelectedItemId() == 0){
                            Toast.makeText(getActivity(), "Mohon Pilih Barang", Toast.LENGTH_SHORT).show();
                        }
                        if(jenis.equals("")){
                            ed_jenis.getEditText().setError("Masukkan Jenis barang");
                        }
                        if(stok.equals("") || !stok.matches("[0-9]+")){
                            ed_stok.getEditText().setError("Masukkan Stok barang hanya angka");
                        }
                    }
                    else{
                        mdbadapter.update_barang(pid, jenis, stok);
                        Toast.makeText(getActivity(), "Barang "+pnama+" Berhasil di Perbarui", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(opsi.equals("delete")){
                    if(spednama.getSelectedItemId() == 0){
                        Toast.makeText(getActivity(), "Mohon Pilih Barang", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mdbadapter.delete_barang(pid);
                        clear();
                        Toast.makeText(getActivity(), "Barang "+pnama+" Sudah Terhapus", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    private void clear(){
        spednama.setSelection(0);
        ed_jenis.getEditText().setText("");
        ed_stok.getEditText().setText("");
        clear_notif();
    }

    private void clear_notif() {
        ed_jenis.getEditText().setError(null);
        ed_stok.getEditText().setError(null);
    }
}