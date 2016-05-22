edflow

    .controller("chatController", function ($scope, $rootScope, $http, $state) {

        $scope.chatItems = [
            {name: "Reddit", message: "Elon Musk on Twitter: @SchaFFFFFF Flight 24 is def capable of flying again, but it makes sense to apply ground delta qual to rocket w toughest entry conditions."},
            {name: "Wikipedia", message: "A shopping mall that stood abandoned for over twice as long as it was in business, until it was finally demolished in 2012. It was featured in the 1980 film The Blues Brothers and became a popular target for urban explorers."},
            {name: "Arxiv", message: "New articles.."},
            {name: "Bot", message: "hi :)"}
        ];

        $scope.placeholder = "";

        console.log("chat");

        $scope.showChat = function () {
            $scope.placeholder = "Write a message...";
            document.querySelector("#chat ul").style.display = "inline";
            document.querySelector("body data").style.display = "none";

            document.querySelector("#chat i.icon").style.display = "none";
            document.querySelector("#chat input").style.opacity = "1";
            document.querySelector("#chat input").focus();
            window.scrollTo(0, document.body.scrollHeight);
        }

        $scope.hideChat = function () {
            $scope.placeholder = "";
            document.querySelector("#chat ul").style.display = "none";
            document.querySelector("body data").style.display = "inline";

            document.querySelector("#chat i.icon").style.display = "inline";
            document.querySelector("#chat input").style.opacity = "0.8";
            document.querySelector("#chat input").blur();
        }

        $scope.send = function () {

            var value = document.querySelector("#chat input").value;

            console.log("send");
            $scope.chatItems.push({name: "You", message: value, self: true});
            document.querySelector("#chat input").value = "";

            $http.get("api/chat?message=" + value).success(function (data) {

            }).error(function () {
                    $rootScope.circleAnimation = "error";
                });
        }
    })

    .directive('ngEnter', function() {
        return function (scope, element, attrs) {
            element.bind("keydown keypress", function (event) {
                if (event.which === 13) {
                    scope.$apply(function () {
                        scope.$eval(attrs.ngEnter, {'event': event});
                    });

                    event.preventDefault();
                }
            });
        };
    });
