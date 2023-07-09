$(document).ready(function() {
  var searchInput = $('#searchInput');
  var searchResults = $('#searchResults');

  searchInput.on('input', function() {
    var keyword = searchInput.val();
    if (keyword.length > 0) {
      $.get('/courses/search', { keyword: keyword }, function(data) {
        searchResults.html(data);
      });
    } else {
      searchResults.empty();
    }
  });
});
