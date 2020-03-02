package io.opentracing.contrib.specialagent.rewrite;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

final class RewriteRules {
  static final String GLOBAL_RULES = "*";

  public static Map<String,RewriteRules> parseRules(final InputStream inputStream) {
    try {
      Map<String,RewriteRules> result = null;
      final JsonObject root = JsonParser.object().from(inputStream);
      final JsonArray globalRuleEntry = root.getArray(GLOBAL_RULES);
      final RewriteRules globalRules = globalRuleEntry == null ? null : parseRules(globalRuleEntry, GLOBAL_RULES);

      for (final String key : root.keySet()) {
        final JsonArray jsonRules = root.getArray(key);
        if (jsonRules == null)
          throw new IllegalArgumentException(key + ": Is not an array");

        if (result == null)
          result = new LinkedHashMap<>();

        final RewriteRules rules = parseRules(jsonRules, key);
        if (globalRules != null)
          rules.addAll(globalRules);

        result.put(key, rules);
      }

      return result != null ? result : Collections.EMPTY_MAP;
    }
    catch (final JsonParserException | PatternSyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  static RewriteRules parseRules(final JsonArray jsonRules, final String key) {
    final RewriteRules rules = new RewriteRules();
    final int size = jsonRules.size();
    for (int i = 0; i < size; ++i) {
      final RewriteRule[] rule = RewriteRule.parseRule(jsonRules.getObject(i), key + ".rules[" + i + "]");
      for (int j = 0; j < rule.length; ++j)
        rules.add(rule[j]);
    }

    return rules;
  }

  final LinkedHashMap<String,List<RewriteRule>> keyToRules = new LinkedHashMap<>();

  private RewriteRules() {
  }

  void add(final RewriteRule rule) {
    List<RewriteRule> list = keyToRules.get(rule.input.getKey());
    if (list == null)
      keyToRules.put(rule.input.getKey(), list = new ArrayList<>());

    list.add(rule);
  }

  void addAll(final RewriteRules rules) {
    for (final List<RewriteRule> value : rules.keyToRules.values())
      for (final RewriteRule rule : value)
        add(rule);
  }

  List<RewriteRule> getRules(final String key) {
    final List<RewriteRule> inputs = keyToRules.get(key);
    return inputs != null ? inputs : Collections.<RewriteRule>emptyList();
  }
}