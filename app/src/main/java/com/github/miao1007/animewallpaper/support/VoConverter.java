package com.github.miao1007.animewallpaper.support;

import com.github.miao1007.animewallpaper.support.api.konachan.DanbooruAPI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leon on 7/21/16.
 */
public class VoConverter {
  private static Map<String, Class> address = new HashMap<>();

  static {
    address.put(DanbooruAPI.KONACHAN, DanbooruAPI.class);
    address.put(DanbooruAPI.YANDE, DanbooruAPI.class);
  }
}
