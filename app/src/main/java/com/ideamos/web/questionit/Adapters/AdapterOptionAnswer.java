package com.ideamos.web.questionit.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.ideamos.web.questionit.R;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creado por Deimer, fecha: 23/06/2016.
 */
public class AdapterOptionAnswer extends RecyclerView.Adapter<AdapterOptionAnswer.ViewHolder>{

    private Context context;
    private List<Integer> data;
    private ViewHolder aHolder;

    public AdapterOptionAnswer(Context mcontext, List<Integer> dataSet) {
        context = mcontext;
        data = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_answer_option, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        aHolder = holder;
        aHolder.txt_option.setId(data.get(position));
        aHolder.txt_option.requestFocus();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void remove(int position) {
        data.remove(position);
        aHolder.txt_option.clearFocus();
        //notifyItemRemoved(position);
    }

    public void add(int id, int position) {
        data.add(position, id);
        //notifyItemInserted(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_option)EditText txt_option;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
