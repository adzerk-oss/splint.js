(function ($) {
  with(splint) {
    // Returns a cell backed by the value of this form input.
    $.fn.cellOf = function() {
      var events         = arguments.length ? arguments : ["change"]
      var valFn          = function(elem) { return elem.is(":checkbox") ? elem.attr("checked") : elem.val(); };
      var returnCell     = cell(valFn(this));
      var resetFn        = function() { reset(returnCell, valFn($(this))); };
      for(idx in events) { this[events[idx]](resetFn); }
      return returnCell;
    };

    // Returns a version of any jQuery method that can accept and
    // react to cell arguments.
    var liftMethod = function(originalFn) {
      return (function () {
        var args = argVec(arguments);
        if(truth(some(isCell, args))) {
          return deref(apply(formula(function() {
            var derefedArgs = toJs(argVec(arguments));
            return originalFn.apply(derefedArgs[0], derefedArgs.slice(1));
          }), this, args));
        } else {
          return originalFn.apply(this, arguments);
        }
      });
    };

    $.fn.toggle      = liftMethod($.fn.toggle);
    $.fn.toggleClass = liftMethod($.fn.toggleClass);
    $.fn.text        = liftMethod($.fn.text);

    // Backward-compatible 'enable' function for jQuery, see
    // http://stackoverflow.com/a/1414366/3998203

    var jqVersionInt = parseInt($.fn.jquery.replace(/\./g, ''));
    var jQueryIsOld  = jqVersionInt < 160;

    var oldEnableFn = function(elem, enabled) {
      enabled ? elem.removeAttr('disabled') : elem.attr('disabled', 'disabled');
    };

    var newEnableFn = function(elem, enabled) {
      elem.prop('disabled', !enabled);
    };

    var enableFn = jQueryIsOld? oldEnableFn : newEnableFn;

    $.fn.cellEnable = function(c) {
      formula(enableFn)(this, c);
      return this;
    };
  }
})(jQuery);
