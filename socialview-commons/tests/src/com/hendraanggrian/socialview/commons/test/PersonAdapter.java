package com.hendraanggrian.socialview.commons.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.hendraanggrian.socialview.commons.SocialAdapter;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class PersonAdapter extends SocialAdapter<Person> {

    @NonNull private final Filter filter = new SocialFilter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Person) resultValue).name;
        }
    };

    public PersonAdapter(@NonNull Context context) {
        super(context, R.layout.item_person, R.id.textViewName);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_person, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Person person = getItem(position);
        if (person != null) {
            holder.textView.setText(person.name);
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class ViewHolder {
        @NonNull private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            textView = (TextView) itemView.findViewById(R.id.textViewName);
        }
    }
}