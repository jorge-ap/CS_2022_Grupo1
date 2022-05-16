require 'calabash-android/calabash_steps'

Given /^I have clicked (\d+) times the coin button$/ do |times|
  for variable in 1..times do
    touch("* id:'bt_moneda'")
  end
end

When /^I click (\d+) times the coin button$/ do |times|
  for variable in 1..times do
    touch("* id:'bt_moneda'")
  end
end
