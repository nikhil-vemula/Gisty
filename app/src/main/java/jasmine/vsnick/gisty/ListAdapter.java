package jasmine.vsnick.gisty;

        import android.app.Application;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.CheckBox;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.eclipse.egit.github.core.Gist;
        import org.eclipse.egit.github.core.GistFile;
        import org.eclipse.egit.github.core.client.GitHubClient;

        import java.util.List;
        import java.util.Map;

public class ListAdapter extends BaseAdapter {
    Context context;
    List<Gist> data;
    private static LayoutInflater inflater = null;
    public ListAdapter(Context context, List<Gist> data)
    {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_item, null);
        final TextView gist_name= (TextView) vi.findViewById(R.id.text1);
        String url;
        Map<String,GistFile> files = data.get(position).getFiles();
        for(Map.Entry<String, GistFile> j :files.entrySet()) {
            String gistName = j.getKey();
            GistFile file = j.getValue();
            gist_name.setText(gistName);
        }
        gist_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="";
                Map<String,GistFile> files = data.get(position).getFiles();
                for(Map.Entry<String, GistFile> j :files.entrySet()) {
                    GistFile file = j.getValue();
                    url = file.getRawUrl();
                }
                Intent intent = new Intent(context,GistView.class);
                intent.putExtra("url",url);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return vi;
    }
}

