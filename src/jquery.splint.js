(function ($) {
  with(splint) {

    var updateCellFrom = function(jqObj, returnCell, events) {
      var valFn   = function(elem) { return elem.is(":checkbox") ? elem.attr("checked") : elem.val(); };
      var resetFn = function() { reset(returnCell, valFn($(jqObj))); };
      resetFn();
      for(idx in events) {
        var f = jqObj[events[idx]];
        if(f === undefined) {
          jqObj[events[idx]] = resetFn;
        } else {
          f.call(jqObj, resetFn);
        }
      }
      return returnCell;
    };

    // Returns a cell backed by the value of this form input.
    $.fn.cellOf = function() { /* (& events) */
      return updateCellFrom(this, cell(null), arguments.length ? arguments : ["change"]);
    };

    // Backs an existing cell with the value of this form input.
    $.fn.updateCell = function() { /* (targetCell & events) */
      var args       = Array.prototype.slice.call(arguments);
      var targetCell = args[0];
      var events     = args.length > 1 ? args.slice(1) : ["change"];
      return updateCellFrom(this, targetCell, events);
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
