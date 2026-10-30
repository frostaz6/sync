# Restored album thumbnail grid

The native multi-image viewer already had an ImageGridFragment and a final
ViewPager page with clickable image previews. Two defects disabled the UX:

1. MultiImageActivity.onCreateOptionsMenu hid menu_grid on all actual album
   images, revealing the button only on the grid page itself.
2. Clicking menu_grid called setCurrentItem(adapter.getCount()), which is one
   position past the last valid page.

The grid button is now visible on each image of multi-image albums (and hidden
inside the grid), and navigates to getCount() - 1. The pager creates the grid
for albums with two or more images and falls back to original image URLs when
thumbnail metadata is unavailable. Tapping a thumbnail sends GridItemSelected
and jumps directly to that image. Empty padding cells do nothing.

## Phone test

1. Open a Reddit gallery with at least two pictures.
2. Tap the grid icon in the upper right of the album viewer.
3. Confirm previews appear; tap the last image and verify that its full-size
   page opens.
4. Open the grid again from the new image. Confirm a different thumbnail
   can be selected.
5. Test a gallery with missing thumbnails and a single-image post.
6. Verify the download icon still appears when viewing an album image.

This is a normal GitHub Actions debug APK until repository permanent signing
has been configured; it cannot update another temporarily signed APK with
a different signing certificate. Export settings before any uninstall.
