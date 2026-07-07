scoreboard objectives add sil_yoni+guxi_energy_data dummy

execute store result score @s sil_yoni+guxi_energy_data run resource get @s sil_yoni:origin_spec/guxi/energy_data

title @s actionbar [{"text":"'","color":"gold"},{"score":{"name":"*","objective":"sil_yoni+guxi_energy_data"},"color":"#66ccff"}]
