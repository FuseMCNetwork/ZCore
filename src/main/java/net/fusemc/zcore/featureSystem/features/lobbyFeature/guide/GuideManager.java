package net.fusemc.zcore.featureSystem.features.lobbyFeature.guide;

import me.michidk.DKLib.FileHelper;
import net.fusemc.zcore.ZCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;

/**
 * Copyright by michidk
 * Created: 21.08.2014.
 */
public class GuideManager {

    private File file = new File(ZCore.getInstance().getDataFolder(), "guide.json");
    public Guide guide;

    public ItemStack book;

    public GuideManager() {
        if (existGuide()) {
            loadGuide();
            createBook();
        }
    }

    private void loadGuide() {
        guide = ZCore.getGson().fromJson(FileHelper.stringFromFile(file), Guide.class);
    }

    private void createBook() {
        book = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle(ChatColor.AQUA + "Erkl\u00E4rung");
        meta.setAuthor(ChatColor.RED + "FuseMC Team");

        StringBuilder pageOne = new StringBuilder();
        pageOne.append(ChatColor.BLUE + "" + ChatColor.UNDERLINE + ChatColor.BOLD + guide.getTitle());
        pageOne.append("\n");
        pageOne.append("\n");
        pageOne.append("\n");

        if (guide.getObjective() != null) {
            pageOne.append(ChatColor.GOLD + "Ziel: ");
            pageOne.append("\n");
            pageOne.append(ChatColor.DARK_GRAY+ guide.getObjective());
            pageOne.append("\n");
            pageOne.append("\n");
        }

        if (guide.getControls() != null) {
            pageOne.append(ChatColor.GOLD + "Steuerung: ");
            pageOne.append("\n");
            pageOne.append(ChatColor.DARK_GRAY + guide.getControls());
            pageOne.append("\n");
        }

        addEmptylines(pageOne,"Ablauf");
        meta.addPage(pageOne.toString());

        if (guide.getOther() != null) {
            StringBuilder pageTwo = new StringBuilder();
            pageTwo.append(ChatColor.GOLD + "Ablauf: ");
            pageTwo.append("\n");
            pageTwo.append(ChatColor.DARK_GRAY + guide.getControls());
            pageTwo.append("\n");

            addEmptylines(pageTwo,"Tipps");

            meta.addPage(pageTwo.toString());
        }


        if(guide.getHints() != null){
            StringBuilder pageThree = new StringBuilder();
            pageThree.append(ChatColor.GOLD + "Tipps: ");
            pageThree.append("\n");
            pageThree.append(ChatColor.DARK_GRAY + guide.getHints());
            pageThree.append("\n");
            meta.addPage(pageThree.toString());
        }

        book.setItemMeta(meta);
    }

    public boolean existGuide() {
        return file.exists();
    }

    public ItemStack getBook() {
        return book;
    }

    private void addEmptylines(StringBuilder stringBuilder, String nextPage){
        for(int i = 0;i<(13-stringBuilder.toString().split("\n").length);i++){
            stringBuilder.append("\n");
        }
        stringBuilder.append(ChatColor.DARK_GRAY + "Nächste Seite: " + nextPage);
    }
}
