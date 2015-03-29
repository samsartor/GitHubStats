package net.eekysam.ghstats.export.presets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import net.eekysam.ghstats.data.RepoEntry;

public abstract class LangExporter<T> extends Exporter
{
	public HashMap<String, T> langs;
	
	public LangExporter(List<RepoEntry> entries)
	{
		super(entries);
	}
	
	@Override
	public void start(List<RepoEntry> entries)
	{
		this.langs = new HashMap<String, T>();
	}
	
	public abstract void startLangs(List<RepoEntry> entries);
	
	@Override
	public void add(RepoEntry repo)
	{
		if (repo.langs != null)
		{
			for (Entry<String, Long> lang : repo.langs.entrySet())
			{
				this.langs.put(lang.getKey(), this.addLang(lang.getKey(), lang.getValue(), this.langs.get(lang.getKey())));
			}
		}
	}
	
	public abstract T addLang(String lang, long bytes, T value);
	
	@Override
	public Table<String, String, Object> end()
	{
		HashBasedTable<String, String, Object> table = HashBasedTable.create();
		for (Entry<String, T> lang : this.langs.entrySet())
		{
			Map<String, ?> row = this.endLang(lang.getKey(), lang.getValue());
			for (Entry<String, ?> entry : row.entrySet())
			{
				table.put(lang.getKey(), entry.getKey(), entry.getValue());
			}
		}
		return table;
	}
	
	public abstract Map<String, ?> endLang(String lang, T value);
}
