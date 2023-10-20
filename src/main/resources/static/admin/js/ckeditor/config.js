/**
 * @license Copyright (c) 2003-2022, CKSource Holding sp. z o.o. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	
//	config.removeButtons = 'Underline,Subscript,Superscript';
	config.removePlugins= ['image', 'uploadimage','about'];
	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre;div';

	// Simplify the dialog windows.
	config.removeDialogTabs = 'link:target;link:advanced;image:Link;image:advanced';
	config.filebrowserUploadMethod = 'form';
//	config.image_previewText = CKEDITOR.tools.repeat(' ', 1);	
//	config.extraPlugins = 'colorbutton';
//	extraPlugins: 'image2,uploadimage',
//	config.extraPlugins = 'image2,uploadimage,colorbutton';
	
	config.extraPlugins = 'embed,autoembed,image2,colorbutton,justify,toc';
	config.embed_provider= '//ckeditor.iframe.ly/api/oembed?url={url}&callback={callback}';
	config.allowedContent = true;
//	config.justifyClasses = [ 'AlignLeft', 'AlignCenter', 'AlignRight', 'AlignJustify' ];
};
